import axios from "axios";

export default axios.create({
  baseURL: "http://i6e103.p.ssafy.io:8080/api",
  headers: {
    "Content-type": "application/json",
  },
});