import os
from pathlib import Path
import http.server
import socketserver

PORT = 8083

directory = (Path(__file__)
    .parent
    .joinpath(Path("garden-front", "dist")
    .resolve()))

os.chdir(directory)

handler = http.server.SimpleHTTPRequestHandler

with socketserver.TCPServer(("", PORT), handler) as httpd:
    print(f"Serving '{directory}' at http://localhost:{PORT}/")
    httpd.serve_forever()
