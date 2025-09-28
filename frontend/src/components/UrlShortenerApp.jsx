import { useState } from 'react';

export default function UrlShortenerApp() {
  const [fullUrl, setFullUrl] = useState('');
  const [alias, setAlias] = useState('');
  const [response, setResponse] = useState('');
  const [allUrls, setAllUrls] = useState([]);

  const apiBase = '/api/url-shortener';

  const handleShorten = async () => {
    setAllUrls([]);
    try {
      const result = await fetch(`${apiBase}/shorten`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ fullUrl, customAlias: alias })
      });
      const data = await result.json();
      if (!result.ok) {
        throw new Error(data.error || 'Error shortening URL');
      }

      setResponse(`Short URL: ${data.shortUrl}`);
    } catch (err) {
      setResponse(err.message);
    }
  };

  const handleGet = async () => {
    setAllUrls([]);
  if (!alias) {
    setResponse('Alias required for GET');
    return;
  }
  try {
    const res = await fetch(`${apiBase}/${alias}`);
    const data = await res.json();

    if (res.status === 302) {
      const fullUrl = data.fullUrl;
      setResponse(`Redirecting to: ${fullUrl}`);
      window.location.href = fullUrl;
    } else if (res.status === 404) {
      setResponse(data.error || 'Alias not found');
    } else {
      setResponse(`Unexpected status: ${res.status}`);
    }
  } catch (err) {
    setResponse(err.message);
  }
};


  const handleDelete = async () => {
    setAllUrls([]);
    if (!alias) {
      setResponse('Alias required for DELETE');
      return;
    }
    try {
      const result = await fetch(`${apiBase}/${alias}`, { method: 'DELETE' });
      if (!result.ok) {
        throw new Error('Custom alias not found');
      }
      setResponse(`Alias ${alias} deleted successfully`);
    } catch (err) {
      setResponse(err.message);
    }
  };

   const handleGetAll = async () => {
    try {
      const res = await fetch(`${apiBase}/urls`);
      if (!res.ok) {
        throw new Error('Error fetching URLs');
      }
      const data = await res.json();
      setAllUrls(data);
      if (data.length > 0) {
      setResponse('Fetched all URLs successfully');
      } else {
      setResponse('No URLs saved');
      }
    } catch (err) {
      setResponse(err.message);
    }
  };

  

  return (
    <div
  style={{
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100vh',          
    width: '100vw',          
    backgroundColor: '#f3f3f3',
  }}
>
  <div
    style={{
      backgroundColor: 'white',
      padding: '24px',
      borderRadius: '8px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
      width: '100%',
      maxWidth: '400px',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
    }}
  >
        <h1 style={{ fontSize: '24px', fontWeight: 'bold', marginBottom: '16px' }}>
          URL Shortener
        </h1>

        <input
          type="text"
          placeholder="Example: https://example.com/very/long/url"
          value={fullUrl}
          onChange={(e) => setFullUrl(e.target.value)}
          style={{
            width: '100%',
            padding: '8px',
            marginBottom: '12px',
            borderRadius: '4px',
            border: '1px solid #ccc',
            outline: 'none',
          }}
        />

        <input
          type="text"
          placeholder="Custom alias (optional)"
          value={alias}
          onChange={(e) => setAlias(e.target.value)}
          style={{
            width: '100%',
            padding: '8px',
            marginBottom: '16px',
            borderRadius: '4px',
            border: '1px solid #ccc',
            outline: 'none',
          }}
        />

        <div style={{ display: 'flex', justifyContent: 'center', gap: '8px', marginBottom: '16px' }}>
          <button
            onClick={handleShorten}
            style={{ backgroundColor: '#3b82f6', color: 'white', padding: '8px 16px', borderRadius: '4px', border: 'none' }}
          >
            Shorten
          </button>
          <button
            onClick={handleGet}
            style={{ backgroundColor: '#22c55e', color: 'white', padding: '8px 16px', borderRadius: '4px', border: 'none' }}
          >
            Get Full URL
          </button>
          <button
            onClick={handleDelete}
            style={{ backgroundColor: '#ef4444', color: 'white', padding: '8px 16px', borderRadius: '4px', border: 'none' }}
          >
            Delete Alias
          </button>

           <button
            onClick={handleGetAll}
            style={{ backgroundColor: '#f59e0b', color: 'white', padding: '8px 16px', borderRadius: '4px', border: 'none' }}
          >
            Get All URLs
          </button>
        </div>

        {response && (
          <div
            style={{
              width: '100%',
              padding: '8px',
              backgroundColor: '#f3f3f3',
              borderRadius: '4px',
              textAlign: 'center',
            }}
          >
            {response}
          </div>
        )}
        {allUrls.length > 0 && (
          <div style={{ width: '100%', maxHeight: '200px', overflowY: 'auto', textAlign: 'left' }}>
           {allUrls.map((url) => {
      return (
        <div key={url.customAlias} style={{ padding: '4px 0', borderBottom: '1px solid #eee' }}>
          <strong>{url.customAlias}</strong> <br />
          Short: <a href={url.shortUrl} target="_blank" rel="noopener noreferrer">{url.shortUrl}</a> <br />
          Full: <a href={url.fullUrl} target="_blank" rel="noopener noreferrer">{url.fullUrl}</a>
        </div>
      );
    })}
          </div>
        )}
      </div>
    </div>
  );
}
