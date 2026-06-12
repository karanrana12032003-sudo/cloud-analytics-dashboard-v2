FROM python:3.9-slim-bullseye
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
RUN apt-get update && apt-get install -y curl
COPY . .
CMD ["python", "app.py"]