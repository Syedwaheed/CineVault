// Fictional movie catalog — original titles & metadata. No real-world IP.
const CV_MOVIES = [
  { id: 'm01', title: 'The Lantern Hours', year: 2024, runtime: 132, rating: 8.7, genres: ['Drama', 'Mystery'], overview: 'A retired clockmaker in coastal Maine discovers a series of letters inside an antique lantern, each addressed to him from a future he hasn\'t lived yet. As the predictions begin to come true, he must decide whether to follow the path written for him.' },
  { id: 'm02', title: 'Glass Cathedral', year: 2025, runtime: 148, rating: 9.1, genres: ['Sci-Fi', 'Thriller'], overview: 'In a near-future Vienna, an architect designs a building that doesn\'t exist on any blueprint. When occupants begin to vanish inside its halls, she is forced to confront the geometry of her own memory.' },
  { id: 'm03', title: 'Salt & Iron', year: 2023, runtime: 118, rating: 7.9, genres: ['Action', 'Drama'], overview: 'Two estranged sisters reunite to operate their late father\'s salvage trawler off the Newfoundland coast — and uncover a manifest that was never meant to be found.' },
  { id: 'm04', title: 'A Quiet Inheritance', year: 2024, runtime: 104, rating: 8.2, genres: ['Drama'], overview: 'When a former concert pianist inherits her grandmother\'s neglected vineyard, she finds the property comes with a tenant who refuses to leave — and a debt she didn\'t know existed.' },
  { id: 'm05', title: 'Northwind', year: 2025, runtime: 141, rating: 8.5, genres: ['Adventure', 'Drama'], overview: 'A solo expedition across the frozen Beaufort Sea becomes a meditation on grief, ambition, and the shape of silence at seventy below.' },
  { id: 'm06', title: 'The Margin', year: 2022, runtime: 96, rating: 7.4, genres: ['Comedy', 'Romance'], overview: 'A pessimistic copy editor and an optimistic cookbook author share an apartment building, an elevator, and exactly opposite opinions on the Oxford comma.' },
  { id: 'm07', title: 'Vespertine', year: 2024, runtime: 127, rating: 8.9, genres: ['Drama', 'Romance'], overview: 'A jazz vocalist returns to her hometown after fifteen years to perform at the wedding of the man she didn\'t marry.' },
  { id: 'm08', title: 'Hollow Crown', year: 2023, runtime: 158, rating: 8.0, genres: ['Action', 'Historical'], overview: 'In 14th-century Bohemia, a disgraced cartographer is conscripted to map a kingdom whose borders are redrawn every season by warring princes.' },
  { id: 'm09', title: 'Cassiopeia', year: 2025, runtime: 112, rating: 7.7, genres: ['Sci-Fi'], overview: 'The five-person crew of a deep-space repair vessel receives a transmission from a ship they decommissioned twenty years ago.' },
  { id: 'm10', title: 'Paper Lions', year: 2024, runtime: 134, rating: 8.4, genres: ['Drama', 'Sport'], overview: 'A high-school chess coach in rural Mississippi takes on a case that will redefine what it means to compete on uneven ground.' },
  { id: 'm11', title: 'The Florist of Rue Lépic', year: 2023, runtime: 109, rating: 8.1, genres: ['Drama', 'Romance'], overview: 'In post-war Paris, a Czech refugee opens a flower shop that becomes the unlikely meeting point for a city trying to remember how to bloom.' },
  { id: 'm12', title: 'Static', year: 2025, runtime: 98, rating: 7.2, genres: ['Thriller', 'Mystery'], overview: 'A late-night radio host begins receiving phone calls from a listener who knows what she did, what she\'ll do, and what she should have done instead.' },
];

const CV_CAST = [
  ['Eliza Vance', 'Margot Hartley'],
  ['Olu Adebayo', 'Detective Brennan'],
  ['Theodora Ng', 'Iris Sandoval'],
  ['Marcus Pell', 'Conor Reeve'],
  ['Sana Karimov', 'Léa Bouchard'],
  ['Henrik Voss', 'Ivar Lindqvist'],
];

const CV_REVIEWS = [
  { author: 'Marina T.', rating: 9, date: '3 days ago', body: 'A patient, beautifully composed film. Vance gives the performance of her career — every silence earns its weight.' },
  { author: 'Owen R.', rating: 8, date: '1 week ago', body: 'The third act loses some momentum, but the cinematography is stunning and the score is haunting.' },
  { author: 'Priya S.', rating: 10, date: '2 weeks ago', body: 'I rarely use the word "perfect," but this comes very close. A film that trusts its audience completely.' },
];

window.CV_MOVIES = CV_MOVIES;
window.CV_CAST = CV_CAST;
window.CV_REVIEWS = CV_REVIEWS;
