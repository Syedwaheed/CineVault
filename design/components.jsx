// CineVault reusable components — MovieCard, RatingBadge, GenreChip,
// SectionHeader, BottomNav, TopBar, plus an Android status-bar adapted
// for the dark cinematic theme.

const c = () => CV.color;

// ─── Status bar (dark) ────────────────────────────────────────
function CVStatusBar({ time = '9:41', dark = true }) {
  const fg = dark ? '#fff' : '#000';
  return (
    <div style={{
      height: 32, display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      padding: '0 16px', position: 'relative', flexShrink: 0,
      fontFamily: CV.font.body,
    }}>
      <div style={{ ...tt('labelL'), color: fg, fontWeight: 600, fontVariantNumeric: 'tabular-nums' }}>{time}</div>
      <div style={{
        position: 'absolute', left: '50%', top: 7, transform: 'translateX(-50%)',
        width: 18, height: 18, borderRadius: 99, background: '#000',
        boxShadow: 'inset 0 0 0 1px rgba(255,255,255,0.06)',
      }} />
      <div style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
        {/* signal bars */}
        <svg width="16" height="11" viewBox="0 0 16 11">
          <rect x="0"  y="7" width="3" height="4" rx="0.5" fill={fg}/>
          <rect x="4"  y="5" width="3" height="6" rx="0.5" fill={fg}/>
          <rect x="8"  y="3" width="3" height="8" rx="0.5" fill={fg}/>
          <rect x="12" y="0" width="3" height="11" rx="0.5" fill={fg} opacity="0.5"/>
        </svg>
        {/* wifi */}
        <svg width="14" height="10" viewBox="0 0 14 10">
          <path d="M7 9.5l1.5-1.5a2 2 0 00-3 0L7 9.5z" fill={fg}/>
          <path d="M3.5 6a5 5 0 017 0" stroke={fg} strokeWidth="1.4" fill="none" strokeLinecap="round"/>
          <path d="M1 3.5a8.5 8.5 0 0112 0" stroke={fg} strokeWidth="1.4" fill="none" strokeLinecap="round"/>
        </svg>
        {/* battery */}
        <svg width="22" height="11" viewBox="0 0 22 11">
          <rect x="0.5" y="0.5" width="18" height="10" rx="2" fill="none" stroke={fg} opacity="0.5"/>
          <rect x="2" y="2" width="14" height="7" rx="1" fill={fg}/>
          <rect x="19.5" y="3.5" width="1.5" height="4" rx="0.6" fill={fg} opacity="0.5"/>
        </svg>
      </div>
    </div>
  );
}

// ─── Bottom nav (CineVault) ───────────────────────────────────
function CVBottomNav({ active = 'home', onChange }) {
  const items = [
    { id: 'home', label: 'Home', icon: 'home' },
    { id: 'search', label: 'Search', icon: 'search' },
    { id: 'watchlist', label: 'Watchlist', icon: 'bookmark' },
    { id: 'profile', label: 'Profile', icon: 'user' },
  ];
  return (
    <div style={{
      flexShrink: 0,
      background: CV.color.bg,
      borderTop: `1px solid ${CV.color.border}`,
      padding: '8px 8px 4px',
      display: 'flex', justifyContent: 'space-around', alignItems: 'center',
    }}>
      {items.map((it) => {
        const sel = active === it.id;
        return (
          <button key={it.id} onClick={() => onChange && onChange(it.id)}
            style={{
              flex: 1, background: 'none', border: 'none', cursor: 'pointer',
              padding: '6px 4px',
              display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4,
            }}>
            <div style={{
              width: 56, height: 28, borderRadius: 14,
              background: sel ? CV.color.primarySoft : 'transparent',
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              transition: 'background 0.15s',
            }}>
              <CVIcon name={it.icon} size={22} color={sel ? CV.color.primary : CV.color.textDim} filled={sel} strokeWidth={sel ? 0 : 1.7}/>
            </div>
            <div style={{
              ...tt('labelL'),
              color: sel ? CV.color.primary : CV.color.textDim,
              fontSize: 11, fontWeight: sel ? 600 : 500,
            }}>{it.label}</div>
          </button>
        );
      })}
    </div>
  );
}

// ─── Top app bar (cinematic) ──────────────────────────────────
function CVTopBar({ title, leading, trailing, transparent = false, scrolled = false }) {
  return (
    <div style={{
      flexShrink: 0,
      height: 56, padding: '0 12px', display: 'flex', alignItems: 'center', gap: 8,
      background: transparent ? 'transparent' : (scrolled ? CV.color.bg : CV.color.bg),
      borderBottom: scrolled && !transparent ? `1px solid ${CV.color.border}` : 'none',
    }}>
      {leading || <div style={{ width: 40 }}/>}
      <div style={{ flex: 1, display: 'flex', alignItems: 'center', gap: 8 }}>
        {typeof title === 'string' ? (
          <div style={{ ...tt('headlineM'), color: CV.color.text }}>{title}</div>
        ) : title}
      </div>
      {trailing}
    </div>
  );
}

// ─── Section header ───────────────────────────────────────────
function CVSectionHeader({ title, action = 'See all', onAction, padding = '0 16px' }) {
  return (
    <div style={{
      display: 'flex', alignItems: 'baseline', justifyContent: 'space-between',
      padding, marginBottom: 12,
    }}>
      <div style={{ ...tt('headlineM'), color: CV.color.text, fontFamily: CV.font.display, fontWeight: 600 }}>{title}</div>
      {action && (
        <button onClick={onAction} style={{
          background: 'none', border: 'none', cursor: 'pointer', padding: 0,
          ...tt('labelL'), color: CV.color.primary,
        }}>{action}</button>
      )}
    </div>
  );
}

// ─── Genre chip ───────────────────────────────────────────────
function CVGenreChip({ label, selected = false, size = 'md', onClick }) {
  const small = size === 'sm';
  return (
    <button onClick={onClick} style={{
      display: 'inline-flex', alignItems: 'center', gap: 4,
      padding: small ? '4px 10px' : '6px 12px',
      borderRadius: CV.radius.pill,
      background: selected ? CV.color.primary : 'transparent',
      border: `1px solid ${selected ? CV.color.primary : CV.color.borderStrong}`,
      color: selected ? '#0A0A0A' : CV.color.textDim,
      ...tt('labelL'),
      fontSize: small ? 11 : 12,
      fontWeight: selected ? 600 : 500,
      cursor: 'pointer', whiteSpace: 'nowrap',
      transition: 'all 0.15s',
    }}>{label}</button>
  );
}

// ─── Rating badge ─────────────────────────────────────────────
// variant: 'star' (gold star + value), 'pill' (filled amber pill), 'imdb' (yellow IMDB-like)
function CVRatingBadge({ rating, variant = 'star', size = 'md' }) {
  const small = size === 'sm';
  const fmt = rating.toFixed(1);
  if (variant === 'pill') {
    return (
      <div style={{
        display: 'inline-flex', alignItems: 'center', gap: 4,
        padding: small ? '2px 8px' : '4px 10px',
        borderRadius: CV.radius.pill,
        background: CV.color.primary,
        color: '#0A0A0A',
        ...tt('labelM'),
        fontSize: small ? 10 : 11,
      }}>
        <CVIcon name="star" size={small ? 10 : 12} color="#0A0A0A" filled strokeWidth={0}/>
        <span style={{ fontFamily: CV.font.mono, fontWeight: 700, letterSpacing: 0 }}>{fmt}</span>
      </div>
    );
  }
  return (
    <div style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}>
      <CVIcon name="star" size={small ? 12 : 14} color={CV.color.star} filled strokeWidth={0}/>
      <span style={{ fontFamily: CV.font.mono, fontSize: small ? 11 : 13, fontWeight: 600, color: CV.color.text }}>{fmt}</span>
    </div>
  );
}

// ─── Movie card (poster + title + rating) ─────────────────────
function CVMovieCard({ movie, width = 124, showRating = true, showTitle = true }) {
  return (
    <div style={{ width, flexShrink: 0 }}>
      <div style={{
        width, height: width * 1.5, borderRadius: CV.radius.md, overflow: 'hidden',
        position: 'relative', background: CV.color.surface,
      }}>
        <CVPoster seed={movie.id} title={movie.title}/>
        {showRating && (
          <div style={{ position: 'absolute', top: 8, left: 8 }}>
            <CVRatingBadge rating={movie.rating} variant="pill" size="sm"/>
          </div>
        )}
      </div>
      {showTitle && (
        <div style={{ marginTop: 10 }}>
          <div style={{ ...tt('titleM'), color: CV.color.text, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{movie.title}</div>
          <div style={{ ...tt('labelL'), color: CV.color.textDim, marginTop: 2, fontFamily: CV.font.mono, fontSize: 11 }}>{movie.year} · {movie.genres[0]}</div>
        </div>
      )}
    </div>
  );
}

// ─── Horizontal scroll row of movie cards ─────────────────────
function CVMovieRow({ movies, cardWidth = 124, padLeft = 16, padRight = 16 }) {
  return (
    <div style={{
      display: 'flex', gap: 12, overflowX: 'auto', overflowY: 'hidden',
      padding: `0 ${padRight}px 4px ${padLeft}px`,
      scrollbarWidth: 'none',
    }}>
      {movies.map((m) => <CVMovieCard key={m.id} movie={m} width={cardWidth}/>)}
    </div>
  );
}

// ─── Primary button ───────────────────────────────────────────
function CVButton({ label, icon, variant = 'primary', size = 'md', onClick, fullWidth }) {
  const big = size === 'lg';
  const styles = {
    primary: { bg: CV.color.primary, fg: '#0A0A0A', border: 'transparent' },
    secondary: { bg: CV.color.surfaceElev, fg: CV.color.text, border: CV.color.borderStrong },
    ghost: { bg: 'transparent', fg: CV.color.text, border: CV.color.borderStrong },
  }[variant];
  return (
    <button onClick={onClick} style={{
      display: 'inline-flex', alignItems: 'center', justifyContent: 'center', gap: 8,
      padding: big ? '12px 20px' : '10px 18px',
      borderRadius: CV.radius.pill,
      background: styles.bg,
      color: styles.fg,
      border: `1px solid ${styles.border}`,
      ...tt('titleM'),
      fontSize: big ? 14 : 13,
      cursor: 'pointer',
      width: fullWidth ? '100%' : 'auto',
      transition: 'transform 0.1s, filter 0.15s',
    }}>
      {icon && <CVIcon name={icon} size={big ? 18 : 16} color={styles.fg} filled={icon === 'play'} strokeWidth={icon === 'play' ? 0 : 1.8}/>}
      <span>{label}</span>
    </button>
  );
}

// ─── Icon button (circular) ───────────────────────────────────
function CVIconButton({ icon, onClick, variant = 'ghost', badge }) {
  const bg = variant === 'tonal' ? 'rgba(20,20,20,0.6)' : 'transparent';
  return (
    <button onClick={onClick} style={{
      width: 40, height: 40, borderRadius: 20, border: 'none', background: bg,
      display: 'flex', alignItems: 'center', justifyContent: 'center', cursor: 'pointer',
      backdropFilter: variant === 'tonal' ? 'blur(8px)' : 'none',
      position: 'relative',
    }}>
      <CVIcon name={icon} size={22} color={CV.color.text} strokeWidth={1.7}/>
      {badge && (
        <div style={{
          position: 'absolute', top: 8, right: 8, width: 8, height: 8, borderRadius: 4,
          background: CV.color.primary, border: `2px solid ${CV.color.bg}`,
        }}/>
      )}
    </button>
  );
}

// ─── Logo lockup ─────────────────────────────────────────────
function CVLogo({ size = 28, showWordmark = true, color }) {
  const fg = color || CV.color.primary;
  return (
    <div style={{ display: 'inline-flex', alignItems: 'center', gap: 8 }}>
      <svg width={size} height={size} viewBox="0 0 28 28" fill="none">
        <rect x="2" y="2" width="24" height="24" rx="6" fill={fg}/>
        <path d="M9 8v12M19 8v12M9 11h10M9 15h10M9 19h10" stroke="#0A0A0A" strokeWidth="1.6" strokeLinecap="round"/>
        <circle cx="14" cy="13.5" r="2.4" fill="#0A0A0A"/>
      </svg>
      {showWordmark && (
        <div style={{ display: 'flex', alignItems: 'baseline', gap: 0 }}>
          <span style={{ ...tt('headlineM'), color: CV.color.text, fontFamily: CV.font.display, letterSpacing: -0.4 }}>Cine</span>
          <span style={{ ...tt('headlineM'), color: fg, fontFamily: CV.font.display, fontStyle: 'italic', letterSpacing: -0.4 }}>Vault</span>
        </div>
      )}
    </div>
  );
}

// ─── Cinematic phone shell — replaces AndroidDevice for dark theme ───
function CVPhone({ children, width = 360, height = 760, dark = true }) {
  return (
    <div style={{
      width, height, borderRadius: 36, overflow: 'hidden',
      background: CV.color.bg,
      border: `1px solid rgba(255,255,255,0.06)`,
      boxShadow: '0 30px 60px rgba(0,0,0,0.4), 0 0 0 8px #1a1a1a, 0 0 0 9px rgba(255,255,255,0.04)',
      display: 'flex', flexDirection: 'column', boxSizing: 'border-box',
      color: CV.color.text,
      fontFamily: CV.font.body,
      position: 'relative',
    }}>
      {children}
    </div>
  );
}

// ─── Gesture nav pill (dark) ─────────────────────────────────
function CVGestureBar() {
  return (
    <div style={{
      flexShrink: 0,
      height: 20, display: 'flex', alignItems: 'center', justifyContent: 'center',
      background: CV.color.bg,
    }}>
      <div style={{ width: 108, height: 4, borderRadius: 2, background: 'rgba(255,255,255,0.5)' }}/>
    </div>
  );
}

Object.assign(window, {
  CVStatusBar, CVBottomNav, CVTopBar, CVSectionHeader,
  CVGenreChip, CVRatingBadge, CVMovieCard, CVMovieRow,
  CVButton, CVIconButton, CVLogo, CVPhone, CVGestureBar,
});
