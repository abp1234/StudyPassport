from fastapi import FastAPI, Depends, HTTPException, Header
from fastapi.responses import PlainTextResponse
# import jwt
import httpx
app = FastAPI()


@app.get("/greeting", response_class=PlainTextResponse)
def say_greeting():
    return " 반가워요!"


SECRET_KEY = "tempSecretKey"
ALGORITHM = "HS256"
def getUser(authorization: str = Header(...)):
    # if not authorization.startswith("Bearer "):
    #     raise HTTPException(status_code=401, detail="Invalid authentication credentials")
    token = authorization[len("Bearer "):]
    try:
        # payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        user_info = "NKJ"#payload.get("user")
        # if user_info is None:
        #     raise HTTPException(status_code=401)
        return user_info
    except:# jwt.PyJWTError as e:
        raise HTTPException(status_code=40) #from e