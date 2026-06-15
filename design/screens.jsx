// CineVault screens — 6 main + supporting artboards.
// Each screen returns a flex column the size of the phone interior.

// ─── Splash ───────────────────────────────────────────────────
function CVSplashScreen() {
  return (
    <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
      background: `radial-gradient(ellipse at 50% 40%, #1a0d05 0%, ${CV.color.bg} 70%)`, position: 'relative', overflow: 'hidden' }}>
      {/* film grain */}
      <svg style={{ position: 'absolute', inset: 0, width: '100%', height: '100%', opacity: 0.5, mixBlendMode: 'overlay' }}>
        <filter id="splash-grain"><feTurbulence baseFrequency="1.5" numOctaves="2"/><feColorMatrix values="0 0 0 0 1  0 0 0 0 1  0 0 0 0 1  0 0 0 0.15 0"/></filter>
        <rect width="100%" height="100%" filter="url(#splash-grain)"/>
      </svg>
      {/* light rays */}
      <div style={{ position: 'absolute', top: -100, left: '50%', transform: 'translateX(-50%) rotate(15deg)',
        width: 4, height: 800, background: 'linear-gradient(to bottom, transparent, rgba(245,166,35,0.18), transparent)' }}/>
      <div style={{ position: 'absolute', top: -100, left: '40%', transform: 'translateX(-50%) rotate(-8deg)',
        width: 3, height: 800, background: 'linear-gradient(to bottom, transparent, rgba(245,166,35,0.08), transparent)' }}/>

      <div style={{ position: 'relative', zIndex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 16 }}>
        <svg width="92" height="92" viewBox="0 0 92 92" fill="none">
          <defs>
            <linearGradient id="splash-grad" x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%" stopColor="#F5A623"/>
              <stop offset="100%" stopColor="#E8C46B"/>
            </linearGradient>
          </defs>
          <rect x="6" y="6" width="80" height="80" rx="20" fill="url(#splash-grad)"/>
          <path d="M28 22v48M64 22v48M28 32h36M28 46h36M28 60h36" stroke="#0A0A0A" strokeWidth="2.4" strokeLinecap="round"/>
          <circle cx="46" cy="40" r="6.5" fill="#0A0A0A"/>
        </svg>
        <div style={{ display: 'flex', alignItems: 'baseline', gap: 0 }}>
          <span style={{ ...tt('displayL'), color: CV.color.text, fontFamily: CV.font.display, fontSize: 40, letterSpacing: -1 }}>Cine</span>
          <span style={{ ...tt('displayL'), color: CV.color.primary, fontFamily: CV.font.display, fontStyle: 'italic', fontSize: 40, letterSpacing: -1 }}>Vault</span>
        </div>
        <div style={{ ...tt('labelM'), color: CV.color.textDim, fontFamily: CV.font.mono, marginTop: 4 }}>EVERY STORY · EVERY FRAME</div>
      </div>

      <div style={{ position: 'absolute', bottom: 56, left: 0, right: 0, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 14 }}>
        <div style={{ width: 32, height: 32, borderRadius: 16, border: `2px solid ${CV.color.borderStrong}`, borderTopColor: CV.color.primary,
          animation: 'cv-spin 0.9s linear infinite' }}/>
        <div style={{ ...tt('labelM'), color: CV.color.textMute }}>v 2.1.0 · TMDB</div>
      </div>
      <style>{`@keyframes cv-spin { to { transform: rotate(360deg); } }`}</style>
    </div>
  );
}

// ─── Hero carousel item (Home) ───────────────────────────────
function CVHero({ movie }) {
  return (
    <div style={{ position: 'relative', width: '100%', height: 420, marginBottom: 8 }}>
      <CVPoster seed={movie.id + '-bd'} variant="backdrop" style={{ position: 'absolute', inset: 0, height: 420 }}/>
      <div style={{ position: 'absolute', inset: 0,
        background: `linear-gradient(to bottom, rgba(10,10,10,0.4) 0%, rgba(10,10,10,0) 30%, rgba(10,10,10,0) 50%, ${CV.color.bg} 100%)` }}/>
      <div style={{ position: 'absolute', left: 16, right: 16, bottom: 16 }}>
        <div style={{ ...tt('labelM'), color: CV.color.primary, marginBottom: 8 }}>★ FEATURED THIS WEEK</div>
        <div style={{ ...tt('displayL'), color: '#fff', fontFamily: CV.font.display, fontSize: 36, lineHeight: 1.05, letterSpacing: -0.6, marginBottom: 10, textShadow: '0 2px 12px rgba(0,0,0,0.6)' }}>{movie.title}</div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 12, flexWrap: 'wrap' }}>
          <CVRatingBadge rating={movie.rating} variant="star"/>
          <div style={{ width: 3, height: 3, borderRadius: 2, background: CV.color.textDim }}/>
          <span style={{ ...tt('labelL'), color: CV.color.textDim, fontFamily: CV.font.mono }}>{movie.year}</span>
          <div style={{ width: 3, height: 3, borderRadius: 2, background: CV.color.textDim }}/>
          <span style={{ ...tt('labelL'), color: CV.color.textDim, fontFamily: CV.font.mono }}>{movie.runtime}m</span>
          <div style={{ width: 3, height: 3, borderRadius: 2, background: CV.color.textDim }}/>
          <span style={{ ...tt('labelL'), color: CV.color.textDim }}>{movie.genres.join(' · ')}</span>
        </div>
        <div style={{ display: 'flex', gap: 10 }}>
          <CVButton label="Watch" icon="play" variant="primary" size="lg"/>
          <CVButton label="Watchlist" icon="plus" variant="secondary" size="lg"/>
        </div>
        {/* page dots */}
        <div style={{ display: 'flex', gap: 6, marginTop: 16, justifyContent: 'center' }}>
          {[0, 1, 2, 3, 4].map((i) => (
            <div key={i} style={{
              width: i === 0 ? 20 : 6, height: 6, borderRadius: 3,
              background: i === 0 ? CV.color.primary : 'rgba(255,255,255,0.25)',
              transition: 'all 0.2s',
            }}/>
          ))}
        </div>
      </div>
    </div>
  );
}

// ─── Home Screen ──────────────────────────────────────────────
function CVHomeScreen() {
  const trending = CV_MOVIES.slice(0, 6);
  const topRated = [...CV_MOVIES].sort((a, b) => b.rating - a.rating).slice(0, 6);
  const newReleases = CV_MOVIES.filter((m) => m.year >= 2024).slice(0, 6);
  const action = CV_MOVIES.filter((m) => m.genres.includes('Action') || m.genres.includes('Adventure'));
  const drama = CV_MOVIES.filter((m) => m.genres.includes('Drama')).slice(0, 6);

  return (
    <React.Fragment>
      <CVStatusBar/>
      <CVTopBar
        leading={<div style={{ paddingLeft: 12 }}><CVLogo size={26}/></div>}
        title={null}
        trailing={<div style={{ display: 'flex' }}>
          <CVIconButton icon="search"/>
          <CVIconButton icon="bell" badge/>
        </div>}
      />
      <div style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden' }}>
        <CVHero movie={CV_MOVIES[1]}/>
        <div style={{ marginTop: 8 }}>
          <CVSectionHeader title="Trending Now"/>
          <CVMovieRow movies={trending}/>
        </div>
        <div style={{ marginTop: 28 }}>
          <CVSectionHeader title="Top Rated"/>
          <CVMovieRow movies={topRated}/>
        </div>
        <div style={{ marginTop: 28 }}>
          <CVSectionHeader title="New Releases"/>
          <CVMovieRow movies={newReleases}/>
        </div>
        <div style={{ marginTop: 28 }}>
          <CVSectionHeader title="Action & Adventure"/>
          <CVMovieRow movies={action}/>
        </div>
        <div style={{ marginTop: 28, marginBottom: 24 }}>
          <CVSectionHeader title="Drama"/>
          <CVMovieRow movies={drama}/>
        </div>
      </div>
      <CVBottomNav active="home"/>
      <CVGestureBar/>
    </React.Fragment>
  );
}

// ─── Detail Screen ────────────────────────────────────────────
function CVDetailScreen() {
  const movie = CV_MOVIES[1]; // Glass Cathedral
  const similar = CV_MOVIES.filter((m) => m.id !== movie.id).slice(0, 6);
  return (
    <React.Fragment>
      {/* Backdrop area */}
      <div style={{ position: 'relative', flexShrink: 0 }}>
        <div style={{ position: 'relative', height: 320, width: '100%' }}>
          <CVPoster seed={movie.id + '-bd2'} variant="backdrop" style={{ height: 320 }}/>
          <div style={{ position: 'absolute', inset: 0,
            background: `linear-gradient(to bottom, rgba(10,10,10,0.4) 0%, rgba(10,10,10,0) 35%, ${CV.color.bg} 95%)` }}/>
          {/* status bar overlay */}
          <div style={{ position: 'absolute', top: 0, left: 0, right: 0 }}>
            <CVStatusBar/>
            <div style={{ height: 56, padding: '0 4px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <CVIconButton icon="arrow-left" variant="tonal"/>
              <div style={{ display: 'flex', gap: 4 }}>
                <CVIconButton icon="share" variant="tonal"/>
                <CVIconButton icon="more" variant="tonal"/>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden', marginTop: -90, position: 'relative', zIndex: 1 }}>
        {/* Poster + meta row */}
        <div style={{ display: 'flex', gap: 14, padding: '0 16px', alignItems: 'flex-end' }}>
          <div style={{ width: 110, height: 165, borderRadius: 10, overflow: 'hidden', flexShrink: 0,
            boxShadow: '0 8px 24px rgba(0,0,0,0.5)', border: `1px solid ${CV.color.border}` }}>
            <CVPoster seed={movie.id} title={movie.title}/>
          </div>
          <div style={{ flex: 1, paddingBottom: 4 }}>
            <div style={{ ...tt('headlineL'), color: CV.color.text, fontFamily: CV.font.display, fontSize: 24, lineHeight: 1.1, letterSpacing: -0.3, marginBottom: 6 }}>{movie.title}</div>
            <div style={{ ...tt('labelL'), color: CV.color.textDim, fontFamily: CV.font.mono, marginBottom: 8 }}>{movie.year} · {movie.runtime}m · PG-13</div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <CVRatingBadge rating={movie.rating}/>
              <span style={{ ...tt('labelL'), color: CV.color.textDim }}>2,847 reviews</span>
            </div>
          </div>
        </div>

        {/* Genres */}
        <div style={{ display: 'flex', gap: 6, padding: '16px 16px 0', flexWrap: 'wrap' }}>
          {movie.genres.map((g) => <CVGenreChip key={g} label={g} size="sm"/>)}
          <CVGenreChip label="Slow-burn" size="sm"/>
        </div>

        {/* CTA row */}
        <div style={{ display: 'flex', gap: 10, padding: '16px 16px 0' }}>
          <CVButton label="Watch trailer" icon="play" variant="primary" size="lg" fullWidth/>
          <CVButton label="" icon="plus" variant="secondary" size="lg"/>
          <CVButton label="" icon="share" variant="secondary" size="lg"/>
        </div>

        {/* Synopsis */}
        <div style={{ padding: '24px 16px 0' }}>
          <div style={{ ...tt('titleL'), color: CV.color.text, marginBottom: 8 }}>Synopsis</div>
          <div style={{ ...tt('bodyL'), color: CV.color.textDim, fontSize: 14 }}>{movie.overview}</div>
        </div>

        {/* Cast */}
        <div style={{ padding: '24px 0 0' }}>
          <CVSectionHeader title="Cast" action="Full crew"/>
          <div style={{ display: 'flex', gap: 14, overflowX: 'auto', padding: '0 16px 4px', scrollbarWidth: 'none' }}>
            {CV_CAST.map(([name, character], i) => {
              const palette = ['#7b2d8e', '#1a4d6e', '#a83232', '#1f5f3f', '#3a4a8e', '#6b3a0a'];
              return (
                <div key={i} style={{ width: 72, flexShrink: 0, textAlign: 'center' }}>
                  <div style={{ width: 64, height: 64, borderRadius: 32, margin: '0 auto 8px',
                    background: `linear-gradient(135deg, ${palette[i % 6]}, ${CV.color.surface})`,
                    display: 'flex', alignItems: 'center', justifyContent: 'center',
                    border: `1px solid ${CV.color.border}`,
                    color: CV.color.text, fontFamily: CV.font.display, fontSize: 22, fontWeight: 600 }}>
                    {name.split(' ').map((n) => n[0]).join('')}
                  </div>
                  <div style={{ ...tt('labelL'), color: CV.color.text, fontSize: 11, fontWeight: 600, marginBottom: 2, lineHeight: 1.2 }}>{name}</div>
                  <div style={{ ...tt('labelM'), color: CV.color.textMute, fontFamily: CV.font.body, textTransform: 'none', letterSpacing: 0, lineHeight: 1.2 }}>{character}</div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Similar */}
        <div style={{ padding: '24px 0 0' }}>
          <CVSectionHeader title="More like this"/>
          <CVMovieRow movies={similar} cardWidth={108}/>
        </div>

        {/* Reviews */}
        <div style={{ padding: '24px 16px 24px' }}>
          <CVSectionHeader title="Reviews" action="See all" padding="0 0 12px"/>
          {CV_REVIEWS.slice(0, 2).map((r, i) => (
            <div key={i} style={{ background: CV.color.surface, borderRadius: 12, padding: 14, marginBottom: 10,
              border: `1px solid ${CV.color.border}` }}>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <div style={{ width: 28, height: 28, borderRadius: 14, background: CV.color.surfaceElev,
                    display: 'flex', alignItems: 'center', justifyContent: 'center',
                    ...tt('labelL'), color: CV.color.primary, fontWeight: 700 }}>{r.author[0]}</div>
                  <div>
                    <div style={{ ...tt('titleM'), color: CV.color.text, fontSize: 12 }}>{r.author}</div>
                    <div style={{ ...tt('labelM'), color: CV.color.textMute }}>{r.date}</div>
                  </div>
                </div>
                <CVRatingBadge rating={r.rating} variant="pill" size="sm"/>
              </div>
              <div style={{ ...tt('bodyM'), color: CV.color.textDim }}>{r.body}</div>
            </div>
          ))}
        </div>
      </div>
      <CVGestureBar/>
    </React.Fragment>
  );
}

// ─── Search Screen ────────────────────────────────────────────
function CVSearchScreen({ empty = false }) {
  const filters = ['All', 'Drama', 'Sci-Fi', 'Action', 'Comedy', 'Mystery'];
  const results = CV_MOVIES.slice(0, 8);
  return (
    <React.Fragment>
      <CVStatusBar/>
      <div style={{ flexShrink: 0, padding: '8px 16px 0' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '10px 14px',
          background: CV.color.surface, borderRadius: CV.radius.pill, border: `1px solid ${CV.color.border}` }}>
          <CVIcon name="search" size={18} color={CV.color.textDim}/>
          <input style={{ flex: 1, background: 'none', border: 'none', outline: 'none',
            color: CV.color.text, ...tt('bodyL'), fontSize: 14 }}
            defaultValue={empty ? 'zzqxx' : 'Lantern'} placeholder="Search movies, actors, directors..."/>
          {!empty && <CVIcon name="close" size={18} color={CV.color.textDim}/>}
        </div>
        <div style={{ display: 'flex', gap: 8, marginTop: 12, overflowX: 'auto', paddingBottom: 4, scrollbarWidth: 'none' }}>
          <button style={{ display: 'inline-flex', alignItems: 'center', gap: 4,
            padding: '6px 12px', borderRadius: CV.radius.pill,
            background: 'transparent', border: `1px solid ${CV.color.borderStrong}`,
            color: CV.color.textDim, ...tt('labelL'), fontSize: 12, cursor: 'pointer', whiteSpace: 'nowrap' }}>
            <CVIcon name="filter" size={13} color={CV.color.textDim}/>Filters
          </button>
          {filters.map((f, i) => <CVGenreChip key={f} label={f} selected={i === 1}/>)}
        </div>
      </div>

      <div style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden', padding: '16px 16px 16px' }}>
        {empty ? (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
            paddingTop: 60, textAlign: 'center' }}>
            <div style={{ width: 120, height: 120, borderRadius: 60, background: CV.color.surface,
              display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: 20,
              border: `1px solid ${CV.color.border}`, position: 'relative' }}>
              <CVIcon name="film" size={48} color={CV.color.textMute}/>
              <div style={{ position: 'absolute', bottom: 8, right: 8, width: 36, height: 36, borderRadius: 18,
                background: CV.color.bg, border: `2px solid ${CV.color.surface}`,
                display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <CVIcon name="search" size={18} color={CV.color.textDim}/>
              </div>
            </div>
            <div style={{ ...tt('headlineM'), color: CV.color.text, fontFamily: CV.font.display, marginBottom: 8 }}>No matches found</div>
            <div style={{ ...tt('bodyM'), color: CV.color.textDim, maxWidth: 240 }}>Try a different keyword, or browse a category to discover something new.</div>
            <div style={{ marginTop: 24 }}>
              <CVButton label="Browse genres" variant="secondary"/>
            </div>
          </div>
        ) : (
          <React.Fragment>
            <div style={{ ...tt('labelL'), color: CV.color.textDim, marginBottom: 12, fontFamily: CV.font.mono, fontSize: 11 }}>
              {results.length} RESULTS · SORTED BY RELEVANCE
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 14 }}>
              {results.map((m) => (
                <div key={m.id}>
                  <div style={{ width: '100%', aspectRatio: '2/3', borderRadius: CV.radius.md, overflow: 'hidden', position: 'relative' }}>
                    <CVPoster seed={m.id} title={m.title}/>
                    <div style={{ position: 'absolute', top: 8, left: 8 }}>
                      <CVRatingBadge rating={m.rating} variant="pill" size="sm"/>
                    </div>
                  </div>
                  <div style={{ marginTop: 8 }}>
                    <div style={{ ...tt('titleM'), color: CV.color.text, fontSize: 13, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{m.title}</div>
                    <div style={{ ...tt('labelL'), color: CV.color.textDim, fontFamily: CV.font.mono, fontSize: 11, marginTop: 2 }}>{m.year} · {m.genres[0]}</div>
                  </div>
                </div>
              ))}
            </div>
          </React.Fragment>
        )}
      </div>
      <CVBottomNav active="search"/>
      <CVGestureBar/>
    </React.Fragment>
  );
}

// ─── Watchlist Screen ─────────────────────────────────────────
function CVWatchlistScreen({ empty = false }) {
  const items = CV_MOVIES.slice(0, 5);
  return (
    <React.Fragment>
      <CVStatusBar/>
      <CVTopBar
        title={<div style={{ ...tt('headlineL'), color: CV.color.text, fontFamily: CV.font.display, fontSize: 22 }}>Watchlist</div>}
        trailing={<CVIconButton icon="filter"/>}
      />
      <div style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden' }}>
        {empty ? (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
            paddingTop: 80, padding: '80px 32px 0', textAlign: 'center' }}>
            <div style={{ width: 132, height: 132, marginBottom: 24, position: 'relative' }}>
              <div style={{ position: 'absolute', inset: 0, borderRadius: 66,
                background: `radial-gradient(circle, ${CV.color.primarySoft} 0%, transparent 70%)` }}/>
              <div style={{ position: 'absolute', inset: 16, borderRadius: 12, background: CV.color.surface,
                border: `1px solid ${CV.color.border}`,
                display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <CVIcon name="bookmark" size={56} color={CV.color.primary} strokeWidth={1.4}/>
              </div>
            </div>
            <div style={{ ...tt('headlineM'), color: CV.color.text, fontFamily: CV.font.display, marginBottom: 8 }}>Your vault is empty</div>
            <div style={{ ...tt('bodyM'), color: CV.color.textDim, marginBottom: 24, maxWidth: 260 }}>
              Save movies to watch later. Tap the bookmark icon on any title to add it here.
            </div>
            <CVButton label="Browse movies" icon="search" variant="primary"/>
          </div>
        ) : (
          <div style={{ padding: '8px 0' }}>
            <div style={{ ...tt('labelL'), color: CV.color.textDim, padding: '8px 16px 12px', fontFamily: CV.font.mono, fontSize: 11 }}>
              {items.length} TITLES · SORTED BY RECENT
            </div>
            {items.map((m, i) => (
              <div key={m.id} style={{ position: 'relative', overflow: 'hidden' }}>
                {/* swipe hint visible on first item */}
                {i === 0 && (
                  <div style={{ position: 'absolute', right: 0, top: 0, bottom: 0, width: 80,
                    background: CV.color.danger, display: 'flex', alignItems: 'center', justifyContent: 'center',
                    color: '#fff', flexDirection: 'column', gap: 4 }}>
                    <CVIcon name="trash" size={20} color="#fff"/>
                    <span style={{ ...tt('labelM'), color: '#fff' }}>REMOVE</span>
                  </div>
                )}
                <div style={{ display: 'flex', gap: 12, padding: '12px 16px',
                  background: CV.color.bg,
                  borderBottom: `1px solid ${CV.color.border}`,
                  transform: i === 0 ? 'translateX(-72px)' : 'none', transition: 'transform 0.2s' }}>
                  <div style={{ width: 60, height: 90, borderRadius: 8, overflow: 'hidden', flexShrink: 0 }}>
                    <CVPoster seed={m.id}/>
                  </div>
                  <div style={{ flex: 1, minWidth: 0, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                    <div>
                      <div style={{ ...tt('titleL'), color: CV.color.text, marginBottom: 4 }}>{m.title}</div>
                      <div style={{ ...tt('labelL'), color: CV.color.textDim, fontFamily: CV.font.mono, fontSize: 11, marginBottom: 6 }}>
                        {m.year} · {m.runtime}m
                      </div>
                      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
                        {m.genres.slice(0, 2).map((g) => (
                          <span key={g} style={{ ...tt('labelM'), color: CV.color.textDim,
                            padding: '2px 8px', borderRadius: 99, background: CV.color.surface,
                            fontFamily: CV.font.body, textTransform: 'none', letterSpacing: 0, fontSize: 10 }}>{g}</span>
                        ))}
                      </div>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: 4 }}>
                      <CVRatingBadge rating={m.rating} size="sm"/>
                      <CVIcon name="chevron-right" size={18} color={CV.color.textMute}/>
                    </div>
                  </div>
                </div>
              </div>
            ))}
            {/* swipe hint */}
            <div style={{ ...tt('labelM'), color: CV.color.textMute, textAlign: 'center', padding: '20px 16px', fontFamily: CV.font.body, textTransform: 'none', letterSpacing: 0, fontSize: 11 }}>
              ← Swipe left to remove
            </div>
          </div>
        )}
      </div>
      <CVBottomNav active="watchlist"/>
      <CVGestureBar/>
    </React.Fragment>
  );
}

// ─── Profile Screen ───────────────────────────────────────────
function CVProfileScreen() {
  const settings = [
    { icon: 'bell', label: 'Notifications', meta: 'On' },
    { icon: 'palette', label: 'Theme', meta: 'Cinematic Dark' },
    { icon: 'globe', label: 'Language', meta: 'English (US)' },
    { icon: 'info', label: 'About CineVault', meta: '' },
    { icon: 'logout', label: 'Sign out', meta: '', danger: true },
  ];
  return (
    <React.Fragment>
      <CVStatusBar/>
      <CVTopBar
        title={<div style={{ ...tt('headlineL'), color: CV.color.text, fontFamily: CV.font.display, fontSize: 22 }}>Profile</div>}
        trailing={<CVIconButton icon="more"/>}
      />
      <div style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden' }}>
        {/* hero */}
        <div style={{ padding: '8px 16px 24px', display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center' }}>
          <div style={{ width: 92, height: 92, borderRadius: 46,
            background: `linear-gradient(135deg, ${CV.color.primary}, ${CV.color.accent})`,
            padding: 3, marginBottom: 14 }}>
            <div style={{ width: '100%', height: '100%', borderRadius: 46, background: CV.color.surface,
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              ...tt('displayM'), color: CV.color.primary, fontFamily: CV.font.display, fontSize: 36 }}>EH</div>
          </div>
          <div style={{ ...tt('headlineL'), color: CV.color.text, fontFamily: CV.font.display, fontSize: 24, marginBottom: 4 }}>Elena Hartwell</div>
          <div style={{ ...tt('labelL'), color: CV.color.textDim, fontFamily: CV.font.mono, fontSize: 11, marginBottom: 16 }}>@elena · MEMBER SINCE 2022</div>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: 6,
            padding: '4px 10px', borderRadius: 99, background: CV.color.primarySoft }}>
            <div style={{ width: 5, height: 5, borderRadius: 3, background: CV.color.primary }}/>
            <span style={{ ...tt('labelM'), color: CV.color.primary, fontFamily: CV.font.body, textTransform: 'none', letterSpacing: 0, fontSize: 11, fontWeight: 600 }}>Cinephile · Tier II</span>
          </div>
        </div>

        {/* stats */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 10, padding: '0 16px', marginBottom: 28 }}>
          {[['247', 'Watched'], ['18', 'Watchlist'], ['34', 'Reviews']].map(([n, l]) => (
            <div key={l} style={{ background: CV.color.surface, borderRadius: 14, padding: '14px 12px', textAlign: 'center',
              border: `1px solid ${CV.color.border}` }}>
              <div style={{ ...tt('displayM'), color: CV.color.primary, fontFamily: CV.font.display, fontSize: 24 }}>{n}</div>
              <div style={{ ...tt('labelL'), color: CV.color.textDim, marginTop: 2, fontSize: 11 }}>{l}</div>
            </div>
          ))}
        </div>

        {/* settings */}
        <div style={{ padding: '0 16px' }}>
          <div style={{ ...tt('labelM'), color: CV.color.textMute, marginBottom: 8, paddingLeft: 4 }}>SETTINGS</div>
          <div style={{ background: CV.color.surface, borderRadius: 14, overflow: 'hidden',
            border: `1px solid ${CV.color.border}` }}>
            {settings.map((s, i) => (
              <div key={s.label} style={{ display: 'flex', alignItems: 'center', gap: 14,
                padding: '14px 16px',
                borderBottom: i < settings.length - 1 ? `1px solid ${CV.color.border}` : 'none' }}>
                <div style={{ width: 32, height: 32, borderRadius: 16,
                  background: s.danger ? 'rgba(255,71,87,0.12)' : CV.color.surfaceElev,
                  display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <CVIcon name={s.icon} size={17} color={s.danger ? CV.color.danger : CV.color.text}/>
                </div>
                <div style={{ flex: 1, ...tt('titleM'), color: s.danger ? CV.color.danger : CV.color.text }}>{s.label}</div>
                {s.meta && <div style={{ ...tt('labelL'), color: CV.color.textDim, fontFamily: CV.font.mono, fontSize: 11 }}>{s.meta}</div>}
                {!s.danger && <CVIcon name="chevron-right" size={16} color={CV.color.textMute}/>}
              </div>
            ))}
          </div>
          <div style={{ ...tt('labelM'), color: CV.color.textMute, textAlign: 'center', padding: '20px 0', fontFamily: CV.font.mono }}>
            CINEVAULT 2.1.0 · BUILD 4178
          </div>
        </div>
      </div>
      <CVBottomNav active="profile"/>
      <CVGestureBar/>
    </React.Fragment>
  );
}

Object.assign(window, {
  CVSplashScreen, CVHomeScreen, CVDetailScreen,
  CVSearchScreen, CVWatchlistScreen, CVProfileScreen,
});
