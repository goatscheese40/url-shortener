import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import './styles/styles.css';
import UrlShortenerApp from './components/UrlShortenerApp';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <UrlShortenerApp/>
  </StrictMode>,
)
