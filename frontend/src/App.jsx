import { startTransition, useDeferredValue, useEffect, useState } from 'react'
import './App.css'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

const eventFormats = ['ON_CAMPUS', 'ONLINE', 'HYBRID']
const registrationStatuses = ['PENDING', 'APPROVED', 'WAITLISTED', 'CANCELLED']
const eventFilters = ['ALL', 'OPEN', 'FULL', 'UNPUBLISHED']

const defaultEventDate = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().slice(0, 10)

const initialClubForm = {
  name: '',
  category: '',
  leadName: '',
  contactEmail: '',
  memberCount: 20,
  active: true,
}

const initialEventForm = {
  clubId: '',
  title: '',
  location: '',
  eventDate: defaultEventDate,
  capacity: 40,
  eventFormat: 'ON_CAMPUS',
  published: true,
  summary: '',
}

const initialRegistrationForm = {
  eventId: '',
  attendeeName: '',
  attendeeEmail: '',
  department: '',
  yearOfStudy: 1,
  status: 'PENDING',
  notes: '',
}

function App() {
  const [dashboard, setDashboard] = useState({ summary: [], pipeline: [], recentRegistrations: [] })
  const [clubs, setClubs] = useState([])
  const [events, setEvents] = useState([])
  const [registrations, setRegistrations] = useState([])
  const [clubForm, setClubForm] = useState(initialClubForm)
  const [eventForm, setEventForm] = useState(initialEventForm)
  const [registrationForm, setRegistrationForm] = useState(initialRegistrationForm)
  const [searchTerm, setSearchTerm] = useState('')
  const [eventFilter, setEventFilter] = useState('ALL')
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [message, setMessage] = useState({ type: 'idle', text: '' })
  const deferredSearch = useDeferredValue(searchTerm)

  const query = deferredSearch.trim().toLowerCase()
  const publishedEvents = events.filter((event) => event.published).length
  const approvedAttendees = registrations.filter((entry) => entry.status === 'APPROVED').length

  const filteredEvents = events.filter((event) => {
    const matchesFilter =
      eventFilter === 'ALL' ||
      (eventFilter === 'OPEN' && event.published && event.seatsLeft > 0) ||
      (eventFilter === 'FULL' && event.published && event.seatsLeft === 0) ||
      (eventFilter === 'UNPUBLISHED' && !event.published)

    const matchesSearch =
      query.length === 0 ||
      event.title.toLowerCase().includes(query) ||
      event.clubName.toLowerCase().includes(query) ||
      event.location.toLowerCase().includes(query) ||
      event.summary.toLowerCase().includes(query)

    return matchesFilter && matchesSearch
  })

  useEffect(() => {
    let cancelled = false

    async function bootstrap() {
      try {
        const [dashboardData, clubData, eventData, registrationData] = await fetchSnapshot()
        if (cancelled) {
          return
        }

        startTransition(() => {
          setDashboard(dashboardData)
          setClubs(clubData)
          setEvents(eventData)
          setRegistrations(registrationData)
        })
      } catch (error) {
        if (!cancelled) {
          setMessage({ type: 'error', text: error.message })
        }
      } finally {
        if (!cancelled) {
          setIsLoading(false)
        }
      }
    }

    bootstrap()

    return () => {
      cancelled = true
    }
  }, [])

  async function handleClubSubmit(event) {
    event.preventDefault()
    setIsSubmitting(true)

    try {
      await apiRequest('/api/clubs', {
        method: 'POST',
        body: JSON.stringify({
          ...clubForm,
          memberCount: Number(clubForm.memberCount),
        }),
      })

      setClubForm(initialClubForm)
      setMessage({ type: 'success', text: 'Club saved successfully.' })
      await refreshData({
        setDashboard,
        setClubs,
        setEvents,
        setRegistrations,
        setIsLoading,
        setMessage,
      })
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  async function handleEventSubmit(event) {
    event.preventDefault()
    setIsSubmitting(true)

    try {
      await apiRequest('/api/events', {
        method: 'POST',
        body: JSON.stringify({
          ...eventForm,
          clubId: Number(eventForm.clubId),
          capacity: Number(eventForm.capacity),
        }),
      })

      setEventForm(initialEventForm)
      setMessage({ type: 'success', text: 'Event created successfully.' })
      await refreshData({
        setDashboard,
        setClubs,
        setEvents,
        setRegistrations,
        setIsLoading,
        setMessage,
      })
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  async function handleRegistrationSubmit(event) {
    event.preventDefault()
    setIsSubmitting(true)

    try {
      await apiRequest('/api/registrations', {
        method: 'POST',
        body: JSON.stringify({
          ...registrationForm,
          eventId: Number(registrationForm.eventId),
          yearOfStudy: Number(registrationForm.yearOfStudy),
        }),
      })

      setRegistrationForm(initialRegistrationForm)
      setMessage({ type: 'success', text: 'Registration added to the event flow.' })
      await refreshData({
        setDashboard,
        setClubs,
        setEvents,
        setRegistrations,
        setIsLoading,
        setMessage,
      })
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="app-shell">
      <header className="topbar panel">
        <div className="topbar-copy">
          <p className="kicker">Campus Hub</p>
          <h1>Club and event operations desk</h1>
          <p className="topbar-text">
            Manage student communities, upcoming events and attendee registrations from one shared
            panel.
          </p>
        </div>
        <div className="topbar-side">
          <article className="status-note">
            <span className="note-label">System</span>
            <strong>{isLoading ? 'Refreshing campus feed' : 'Campus feed is live'}</strong>
            <p>Local Spring Boot API and React dashboard are connected.</p>
          </article>
          <article className="status-note">
            <span className="note-label">Snapshot</span>
            <strong>
              {clubs.length} clubs, {publishedEvents} published events
            </strong>
            <p>{approvedAttendees} attendees are currently approved across live events.</p>
          </article>
        </div>
      </header>

      {message.text ? (
        <div className={`alert alert-${message.type}`}>
          <span>{message.text}</span>
          <button type="button" onClick={() => setMessage({ type: 'idle', text: '' })}>
            Dismiss
          </button>
        </div>
      ) : null}

      <section className="summary-row">
        {dashboard.summary.map((card) => (
          <article key={card.label} className={`summary-card accent-${card.accent}`}>
            <span className="summary-label">{card.label}</span>
            <strong>{card.value}</strong>
          </article>
        ))}
      </section>

      <section className="workspace">
        <main className="workspace-main">
          <section className="panel tracker-panel">
            <div className="panel-header panel-header-wide">
              <div>
                <p className="section-tag">Event board</p>
                <h2>Upcoming sessions</h2>
                <p className="panel-copy">
                  Review capacity, format, publication status and event notes without leaving the
                  operations board.
                </p>
              </div>
              <div className="toolbar">
                <input
                  value={searchTerm}
                  onChange={(event) => setSearchTerm(event.target.value)}
                  placeholder="Search event, club or location"
                />
                <select value={eventFilter} onChange={(event) => setEventFilter(event.target.value)}>
                  {eventFilters.map((option) => (
                    <option key={option} value={option}>
                      {option === 'ALL' ? 'All events' : formatLabel(option)}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="pipeline-strip">
              {dashboard.pipeline.map((item) => (
                <div key={item.status} className="pipeline-chip">
                  <span>{formatLabel(item.status)}</span>
                  <strong>{item.total}</strong>
                </div>
              ))}
            </div>

            <div className="application-list">
              {filteredEvents.length > 0 ? (
                filteredEvents.map((event) => {
                  const eventState = getEventState(event)

                  return (
                    <article key={event.id} className="application-card">
                      <div className="application-top">
                        <div>
                          <p className="application-name">{event.title}</p>
                          <p className="application-role">
                            {event.clubName} - {event.location}
                          </p>
                        </div>
                        <span className={`status-pill ${eventState.className}`}>
                          {eventState.label}
                        </span>
                      </div>
                      <div className="application-meta">
                        <span>{formatLabel(event.eventFormat)}</span>
                        <span>{formatDate(event.eventDate)}</span>
                        <span>{event.seatsLeft} seats left</span>
                      </div>
                      <p className="application-notes">{event.summary}</p>
                      <div className="application-footer">
                        <span className="muted">
                          Approved {event.approvedCount} / Capacity {event.capacity}
                        </span>
                        <span className="muted">
                          {event.published ? 'Visible to students' : 'Draft event'}
                        </span>
                      </div>
                    </article>
                  )
                })
              ) : (
                <div className="empty-state">
                  <strong>No events match this filter.</strong>
                  <p>Try another event state or clear the search field.</p>
                </div>
              )}
            </div>
          </section>

          <section className="resource-grid">
            <section className="panel compact-panel">
              <div className="panel-header">
                <div>
                  <p className="section-tag">Clubs</p>
                  <h2>Community roster</h2>
                </div>
                <span className="muted">{clubs.length} total</span>
              </div>
              <div className="stack-list">
                {clubs.map((club) => (
                  <article key={club.id} className="mini-card">
                    <strong>{club.name}</strong>
                    <p>
                      {club.category} - {club.memberCount} members
                    </p>
                    <span>
                      Lead {club.leadName} - {club.active ? 'Active' : 'Paused'}
                    </span>
                  </article>
                ))}
              </div>
            </section>

            <section className="panel compact-panel">
              <div className="panel-header">
                <div>
                  <p className="section-tag">Recent activity</p>
                  <h2>Registration log</h2>
                </div>
                <span className="muted">{dashboard.recentRegistrations.length} recent</span>
              </div>
              <div className="stack-list">
                {dashboard.recentRegistrations.map((entry) => (
                  <article key={entry.id} className="mini-card">
                    <strong>
                      {entry.attendeeName} - {entry.eventTitle}
                    </strong>
                    <p>
                      {entry.department} - Year {entry.yearOfStudy}
                    </p>
                    <span>
                      {formatLabel(entry.status)} - {formatDateTime(entry.createdAt)}
                    </span>
                  </article>
                ))}
              </div>
            </section>
          </section>
        </main>

        <aside className="workspace-side">
          <section className="panel form-panel">
            <div className="panel-header">
              <div>
                <p className="section-tag">Clubs</p>
                <h2>Add club</h2>
              </div>
            </div>
            <form className="form-grid" onSubmit={handleClubSubmit}>
              <label>
                Club name
                <input
                  value={clubForm.name}
                  onChange={(event) =>
                    setClubForm((current) => ({ ...current, name: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Category
                <input
                  value={clubForm.category}
                  onChange={(event) =>
                    setClubForm((current) => ({ ...current, category: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Lead name
                <input
                  value={clubForm.leadName}
                  onChange={(event) =>
                    setClubForm((current) => ({ ...current, leadName: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Contact email
                <input
                  type="email"
                  value={clubForm.contactEmail}
                  onChange={(event) =>
                    setClubForm((current) => ({
                      ...current,
                      contactEmail: event.target.value,
                    }))
                  }
                  required
                />
              </label>
              <label>
                Member count
                <input
                  type="number"
                  min="1"
                  value={clubForm.memberCount}
                  onChange={(event) =>
                    setClubForm((current) => ({ ...current, memberCount: event.target.value }))
                  }
                  required
                />
              </label>
              <label className="checkbox-row">
                <input
                  type="checkbox"
                  checked={clubForm.active}
                  onChange={(event) =>
                    setClubForm((current) => ({ ...current, active: event.target.checked }))
                  }
                />
                Active club
              </label>
              <button className="primary-button" disabled={isSubmitting} type="submit">
                Save club
              </button>
            </form>
          </section>

          <section className="panel form-panel">
            <div className="panel-header">
              <div>
                <p className="section-tag">Events</p>
                <h2>Create event</h2>
              </div>
            </div>
            <form className="form-grid" onSubmit={handleEventSubmit}>
              <label className="wide">
                Club
                <select
                  value={eventForm.clubId}
                  onChange={(event) =>
                    setEventForm((current) => ({ ...current, clubId: event.target.value }))
                  }
                  required
                >
                  <option value="">Select club</option>
                  {clubs.map((club) => (
                    <option key={club.id} value={club.id}>
                      {club.name}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Title
                <input
                  value={eventForm.title}
                  onChange={(event) =>
                    setEventForm((current) => ({ ...current, title: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Location
                <input
                  value={eventForm.location}
                  onChange={(event) =>
                    setEventForm((current) => ({ ...current, location: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Event date
                <input
                  type="date"
                  value={eventForm.eventDate}
                  onChange={(event) =>
                    setEventForm((current) => ({ ...current, eventDate: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Capacity
                <input
                  type="number"
                  min="1"
                  value={eventForm.capacity}
                  onChange={(event) =>
                    setEventForm((current) => ({ ...current, capacity: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Format
                <select
                  value={eventForm.eventFormat}
                  onChange={(event) =>
                    setEventForm((current) => ({ ...current, eventFormat: event.target.value }))
                  }
                >
                  {eventFormats.map((option) => (
                    <option key={option} value={option}>
                      {formatLabel(option)}
                    </option>
                  ))}
                </select>
              </label>
              <label className="checkbox-row">
                <input
                  type="checkbox"
                  checked={eventForm.published}
                  onChange={(event) =>
                    setEventForm((current) => ({ ...current, published: event.target.checked }))
                  }
                />
                Publish event
              </label>
              <label className="wide">
                Summary
                <textarea
                  rows="4"
                  value={eventForm.summary}
                  onChange={(event) =>
                    setEventForm((current) => ({ ...current, summary: event.target.value }))
                  }
                  placeholder="Outline the event goal, audience and session plan."
                  required
                />
              </label>
              <button className="primary-button" disabled={isSubmitting} type="submit">
                Save event
              </button>
            </form>
          </section>

          <section className="panel form-panel">
            <div className="panel-header">
              <div>
                <p className="section-tag">Attendance</p>
                <h2>Add registration</h2>
              </div>
            </div>
            <form className="form-grid" onSubmit={handleRegistrationSubmit}>
              <label className="wide">
                Event
                <select
                  value={registrationForm.eventId}
                  onChange={(event) =>
                    setRegistrationForm((current) => ({
                      ...current,
                      eventId: event.target.value,
                    }))
                  }
                  required
                >
                  <option value="">Select event</option>
                  {events.map((event) => (
                    <option key={event.id} value={event.id}>
                      {event.title} - {event.clubName}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Attendee name
                <input
                  value={registrationForm.attendeeName}
                  onChange={(event) =>
                    setRegistrationForm((current) => ({
                      ...current,
                      attendeeName: event.target.value,
                    }))
                  }
                  required
                />
              </label>
              <label>
                Attendee email
                <input
                  type="email"
                  value={registrationForm.attendeeEmail}
                  onChange={(event) =>
                    setRegistrationForm((current) => ({
                      ...current,
                      attendeeEmail: event.target.value,
                    }))
                  }
                  required
                />
              </label>
              <label>
                Department
                <input
                  value={registrationForm.department}
                  onChange={(event) =>
                    setRegistrationForm((current) => ({
                      ...current,
                      department: event.target.value,
                    }))
                  }
                  required
                />
              </label>
              <label>
                Year of study
                <input
                  type="number"
                  min="1"
                  max="8"
                  value={registrationForm.yearOfStudy}
                  onChange={(event) =>
                    setRegistrationForm((current) => ({
                      ...current,
                      yearOfStudy: event.target.value,
                    }))
                  }
                  required
                />
              </label>
              <label>
                Status
                <select
                  value={registrationForm.status}
                  onChange={(event) =>
                    setRegistrationForm((current) => ({ ...current, status: event.target.value }))
                  }
                >
                  {registrationStatuses.map((option) => (
                    <option key={option} value={option}>
                      {formatLabel(option)}
                    </option>
                  ))}
                </select>
              </label>
              <label className="wide">
                Notes
                <textarea
                  rows="4"
                  value={registrationForm.notes}
                  onChange={(event) =>
                    setRegistrationForm((current) => ({ ...current, notes: event.target.value }))
                  }
                  placeholder="Attendance note, interest area or waitlist reason."
                  required
                />
              </label>
              <button className="primary-button" disabled={isSubmitting} type="submit">
                Save registration
              </button>
            </form>
          </section>
        </aside>
      </section>
    </div>
  )
}

async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {}),
    },
    ...options,
  })

  if (!response.ok) {
    const errorPayload = await response.json().catch(() => ({}))
    const message =
      errorPayload.message ||
      (errorPayload.validationErrors
        ? Object.values(errorPayload.validationErrors).join(', ')
        : 'Request failed')
    throw new Error(message)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

async function fetchSnapshot() {
  return Promise.all([
    apiRequest('/api/dashboard'),
    apiRequest('/api/clubs'),
    apiRequest('/api/events'),
    apiRequest('/api/registrations'),
  ])
}

async function refreshData({
  setDashboard,
  setClubs,
  setEvents,
  setRegistrations,
  setIsLoading,
  setMessage,
}) {
  setIsLoading(true)

  try {
    const [dashboardData, clubData, eventData, registrationData] = await fetchSnapshot()

    startTransition(() => {
      setDashboard(dashboardData)
      setClubs(clubData)
      setEvents(eventData)
      setRegistrations(registrationData)
    })
  } catch (error) {
    setMessage({ type: 'error', text: error.message })
  } finally {
    setIsLoading(false)
  }
}

function getEventState(event) {
  if (!event.published) {
    return { label: 'Draft', className: 'status-new' }
  }

  if (event.seatsLeft === 0) {
    return { label: 'Full', className: 'status-rejected' }
  }

  return { label: 'Open', className: 'status-hired' }
}

function formatLabel(value) {
  return value
    .toLowerCase()
    .split('_')
    .map((word) => word[0].toUpperCase() + word.slice(1))
    .join(' ')
}

function formatDate(value) {
  return new Intl.DateTimeFormat('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  }).format(new Date(value))
}

function formatDateTime(value) {
  return new Intl.DateTimeFormat('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(value))
}

export default App
