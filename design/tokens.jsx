// CineVault design tokens — single source of truth.
// All screens read from window.CV. Tweaks panel mutates a few entries live.

const CV = {
  // Color
  color: {
    bg: '#0A0A0A',          // app background (near-black, OLED-friendly)
    surface: '#141414',     // cards
    surfaceElev: '#1F1F1F', // elevated chips, buttons
    surfaceHi: '#2A2A2A',   // highest elevation, hover
    border: 'rgba(255,255,255,0.08)',
    borderStrong: 'rgba(255,255,255,0.14)',

    text: '#F5F5F5',
    textDim: '#A3A3A3',
    textMute: '#6B6B6B',

    primary: '#F5A623',     // amber accent
    primaryDim: '#C9881A',
    primarySoft: 'rgba(245,166,35,0.14)',
    accent: '#E8C46B',      // warm gold
    danger: '#FF4757',
    success: '#3DDC97',

    // rating colors
    star: '#F5A623',
    critical: '#FF6B35',
  },

  // Typography
  font: {
    display: '"Playfair Display", "Times New Roman", Georgia, serif',
    body: '"Inter Tight", "Inter", system-ui, sans-serif',
    mono: '"JetBrains Mono", "SF Mono", Menlo, monospace',
  },

  // Type scale (Material 3 inspired, dialled for a cinematic app)
  type: {
    displayL: { size: 36, lh: 1.1, weight: 600, family: 'display', tracking: -0.5 },
    displayM: { size: 28, lh: 1.15, weight: 600, family: 'display', tracking: -0.3 },
    headlineL: { size: 22, lh: 1.25, weight: 600, family: 'body', tracking: -0.2 },
    headlineM: { size: 18, lh: 1.3, weight: 600, family: 'body', tracking: -0.1 },
    titleL: { size: 16, lh: 1.4, weight: 600, family: 'body', tracking: 0 },
    titleM: { size: 14, lh: 1.4, weight: 600, family: 'body', tracking: 0 },
    bodyL: { size: 15, lh: 1.5, weight: 400, family: 'body', tracking: 0 },
    bodyM: { size: 13, lh: 1.5, weight: 400, family: 'body', tracking: 0 },
    labelL: { size: 12, lh: 1.3, weight: 500, family: 'body', tracking: 0.3 },
    labelM: { size: 10, lh: 1.3, weight: 600, family: 'mono', tracking: 1.2 },
  },

  // Radius / Spacing
  radius: { xs: 4, sm: 8, md: 12, lg: 16, xl: 20, pill: 999 },
  space: { 1: 4, 2: 8, 3: 12, 4: 16, 5: 20, 6: 24, 8: 32, 10: 40 },
};

// Typography helper — apply a token to inline style
function tt(token, family) {
  const t = CV.type[token];
  if (!t) return {};
  return {
    fontSize: t.size,
    lineHeight: t.lh,
    fontWeight: t.weight,
    fontFamily: CV.font[family || t.family],
    letterSpacing: typeof t.tracking === 'number' ? `${t.tracking}px` : 0,
  };
}

window.CV = CV;
window.tt = tt;
