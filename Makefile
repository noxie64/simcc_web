up:
	docker compose --env-file ./backend/.env up

down:
	docker compose --env-file ./backend/.env down

down-clean:
	docker compose --env-file ./backend/.env down -v
