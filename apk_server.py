#!/usr/bin/env python3
import http.server
import socketserver
import os
import sys

PORT = 3000
APK_PATH = os.path.abspath("app/build/outputs/apk/debug/app-debug.apk")

HTML_TEMPLATE = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Daily American - Download APK</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700;800&display=swap" rel="stylesheet">
  <style>
    :root {
      --primary: #0284c7;
      --primary-dark: #0369a1;
      --bg: #f8fafc;
      --card-bg: #ffffff;
      --text: #0f172a;
      --subtext: #475569;
      --border: #e2e8f0;
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
      background-color: var(--bg);
      color: var(--text);
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 24px;
    }
    .card {
      background: var(--card-bg);
      border-radius: 24px;
      box-shadow: 0 20px 40px -15px rgba(0,0,0,0.08), 0 0 0 1px var(--border);
      max-width: 480px;
      width: 100%;
      padding: 36px 28px;
      text-align: center;
    }
    .badge {
      display: inline-block;
      background: #e0f2fe;
      color: #0369a1;
      font-weight: 700;
      font-size: 0.8rem;
      text-transform: uppercase;
      letter-spacing: 0.05em;
      padding: 6px 14px;
      border-radius: 9999px;
      margin-bottom: 16px;
    }
    .icon {
      width: 80px;
      height: 80px;
      border-radius: 20px;
      background: linear-gradient(135deg, #0284c7 0%, #2563eb 100%);
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 38px;
      margin: 0 auto 20px auto;
      box-shadow: 0 10px 25px -5px rgba(2, 132, 199, 0.4);
    }
    h1 {
      font-size: 1.75rem;
      font-weight: 800;
      color: var(--text);
      margin-bottom: 8px;
      letter-spacing: -0.02em;
    }
    p.subtitle {
      font-size: 0.95rem;
      color: var(--subtext);
      line-height: 1.5;
      margin-bottom: 28px;
    }
    .btn-download {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 12px;
      background: linear-gradient(135deg, #0284c7 0%, #0369a1 100%);
      color: #ffffff;
      text-decoration: none;
      font-weight: 700;
      font-size: 1.15rem;
      padding: 18px 28px;
      border-radius: 16px;
      box-shadow: 0 10px 20px -3px rgba(2, 132, 199, 0.4);
      cursor: pointer;
    }
    .btn-download:hover {
      background: linear-gradient(135deg, #0369a1 0%, #075985 100%);
    }
    .btn-sub {
      font-size: 0.8rem;
      opacity: 0.85;
      font-weight: 500;
      margin-top: 2px;
    }
    .instructions {
      background: #f8fafc;
      border: 1px solid var(--border);
      border-radius: 16px;
      padding: 20px;
      margin-top: 28px;
      text-align: left;
    }
    .instructions h3 {
      font-size: 0.9rem;
      font-weight: 700;
      color: var(--text);
      margin-bottom: 12px;
      text-transform: uppercase;
      letter-spacing: 0.04em;
    }
    .step {
      display: flex;
      gap: 12px;
      margin-bottom: 10px;
      font-size: 0.88rem;
      color: var(--subtext);
      line-height: 1.4;
    }
    .step:last-child { margin-bottom: 0; }
    .step-num {
      background: #e2e8f0;
      color: #334155;
      font-weight: 700;
      width: 22px;
      height: 22px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 0.75rem;
      flex-shrink: 0;
    }
    .qr-container {
      margin-top: 24px;
      padding-top: 20px;
      border-top: 1px dashed var(--border);
      display: flex;
      flex-direction: column;
      align-items: center;
    }
    .qr-container img {
      width: 140px;
      height: 140px;
      border-radius: 12px;
      border: 1px solid var(--border);
      padding: 6px;
      background: white;
    }
    .qr-text {
      font-size: 0.8rem;
      color: var(--subtext);
      margin-top: 8px;
    }
  </style>
</head>
<body>
  <div class="card">
    <div class="icon">🇺🇸</div>
    <span class="badge">Direct Android APK</span>
    <h1>Daily American</h1>
    <p class="subtitle">AI English Tutor & American Daily Life</p>

    <a href="/DailyAmerican.apk" download="DailyAmerican.apk" class="btn-download">
      <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
        <polyline points="7 10 12 15 17 10"></polyline>
        <line x1="12" y1="15" x2="12" y2="3"></line>
      </svg>
      <div>
        <div>Download APK</div>
        <div class="btn-sub">33 MB • Tap to Install</div>
      </div>
    </a>

    <div class="instructions">
      <h3>Install in 3 Steps:</h3>
      <div class="step">
        <div class="step-num">1</div>
        <div>Tap the <strong>Download APK</strong> button above on your Android phone.</div>
      </div>
      <div class="step">
        <div class="step-num">2</div>
        <div>When downloaded, tap the file in your notifications or <strong>Downloads</strong> folder.</div>
      </div>
      <div class="step">
        <div class="step-num">3</div>
        <div>Tap <strong>Install</strong> (toggle <em>Allow from this source</em> if prompted).</div>
      </div>
    </div>

    <div class="qr-container">
      <img src="https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=https://ais-pre-ahdax7hcn44utd5cqrj2kp-596498626522.europe-west2.run.app/DailyAmerican.apk" alt="Scan with phone" />
      <div class="qr-text">Scan with phone camera to download directly</div>
    </div>
  </div>
</body>
</html>
"""

class ApkHandler(http.server.BaseHTTPRequestHandler):
    def do_HEAD(self):
        self.handle_req(is_head=True)

    def do_GET(self):
        self.handle_req(is_head=False)

    def handle_req(self, is_head=False):
        try:
            path = self.path.split('?')[0]
            if path in ['/DailyAmerican.apk', '/app-debug.apk']:
                if not os.path.exists(APK_PATH):
                    self.send_error(404, "APK not found. Please rebuild.")
                    return
                file_size = os.path.getsize(APK_PATH)
                self.send_response(200)
                self.send_header("Content-Type", "application/vnd.android.package-archive")
                self.send_header("Content-Disposition", 'attachment; filename="DailyAmerican.apk"')
                self.send_header("Content-Length", str(file_size))
                self.send_header("Accept-Ranges", "bytes")
                self.send_header("Cache-Control", "no-cache")
                self.end_headers()
                if not is_head:
                    with open(APK_PATH, "rb") as f:
                        while chunk := f.read(131072):
                            self.wfile.write(chunk)
                            self.wfile.flush()
            else:
                data = HTML_TEMPLATE.encode("utf-8")
                self.send_response(200)
                self.send_header("Content-Type", "text/html; charset=utf-8")
                self.send_header("Content-Length", str(len(data)))
                self.end_headers()
                if not is_head:
                    self.wfile.write(data)
                    self.wfile.flush()
        except (BrokenPipeError, ConnectionResetError):
            pass
        except Exception as e:
            sys.stderr.write(f"Handler error: {e}\n")

    def log_message(self, format, *args):
        pass

class ReusableServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    allow_reuse_address = True
    daemon_threads = True

if __name__ == "__main__":
    while True:
        try:
            with ReusableServer(("0.0.0.0", PORT), ApkHandler) as httpd:
                httpd.serve_forever()
        except Exception as err:
            sys.stderr.write(f"Server loop error: {err}\n")
            import time
            time.sleep(1)
