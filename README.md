# SimCC 🦠
Simple Command and Control Server  
**⚠️ This software is merely a prove-of-concept and should only be used for education purposes ⚠️**

## Running
### WINDOWS ONLY
We use [traefik](https://hub.docker.com/_/traefik/) as a revers-proxy.
For it to work, you must the `DOCKER_HOST` env-var in your `backend/.env` to `//./pipe/docker_engine`.
### URLs
- 📝 PGAdmin: [http://localhost:7777/pgadmin](http://localhost:7777/pgadmin)
- 📝 Redis-Insight: [http://localhost:7777/redis](http://localhost:7777/redis)
- 🖼️ Frontend: [http://localhost:7777/](http://localhost:7777/)
- ⚙️ Backend: [http://localhost:7777/api/](http://localhost:7777/api/)
