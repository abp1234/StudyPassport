from fastapi import FastAPI, Depends, HTTPException, Header
from fastapi.responses import PlainTextResponse
import jwt
import httpx
app = FastAPI()

@app.get("/hello", response_class=PlainTextResponse)
def say_hello():
    return "안녕"




SECRET_KEY = 'tempSecretKey'
ALGORITHM = 'HS512'
def getUser(authorization: str = Header(...)):
    print(authorization)
    if not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Invalid authentication credentials")
    token = authorization[len("Bearer "):].strip()
    # payload = {"sub": "NKJ", "iat": 1739135807, "exp": 1739139407}
    # token = jwt.encode(payload, SECRET_KEY.encode("utf-8"), algorithm=ALGORITHM)
    print(token)
    # token="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJOS0oiLCJpYXQiOjE3MzkxMzkxODcsImV4cCI6MTczOTE0Mjc4N30.Kfy7oB51IHFwT4mePQ0AYQBj8UQlsEFhn-GRlMEvbhVLzE797R08h_QPqs2ivkteXdM18ycrxlWp6Xv2xLIoJg"
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        print(f"Decoded payload: {payload}")
        user_info = payload.get("sub")
        if user_info is None:
            raise HTTPException(status_code=401)
        return user_info
    except jwt.PyJWTError as e:
        print("JWT decoding failed:", str(e))
        raise HTTPException(status_code=401, detail="토크 왜 이럼") from e
    

@app.get("/hello/transaction", response_class=PlainTextResponse)
async def transaction(user:str = Depends(getUser)):

    result1 = "안녕"
    async with httpx.AsyncClient() as client:
        response2 = await client.get("http://localhost:8083/hi")
        result2 = response2.text
        response3 = await client.get("http://localhost:8084/greeting")
        result3 = response3.text
    aggregated_result = f"{result1} {result2} {result3}"
    return f"{aggregated_result}, {user}!"