import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "/";

class AUthServiceAziende {
    aziende() {
        return axios
        .get(API_URL + "aziende/react", { headers: authHeader() } )

    }
}

export default new AUthServiceAziende();