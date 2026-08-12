import express from "express";
import cors from "cors";
import os from "os";
import mongoose from "mongoose";
import { Eureka } from "eureka-js-client";
import dotenv from "dotenv";
import vehicleRoutes from "./routes/vehicle.routes";

dotenv.config();

const app = express();
const PORT = Number(process.env.PORT) || 8082;

app.use(cors());
app.use(express.json());

app.use("/api/v1/vehicles", vehicleRoutes);

mongoose
    .connect(process.env.MONGO_URI as string)
    .then(() => console.log("MongoDB connected"))
    .catch((error) => console.log("MONGO_CONNECTION ERROR:", error));

app.listen(PORT, () => {
    console.log(`Vehicle Service running on port ${PORT}`);

    const hostName = os.hostname();

    const eurekaClient = new Eureka({
        instance: {
            app: "vehicle-service",
            instanceId: `${hostName}:vehicle-service:${PORT}`,
            hostName: hostName,
            ipAddr: "127.0.0.1",
            port: {
                "$": PORT,
                "@enabled": true,
            },
            vipAddress: "vehicle-service",
            dataCenterInfo: {
                "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                name: "MyOwn",
            },
        },
        eureka: {
            host: process.env.EUREKA_HOST || "localhost",
            port: Number(process.env.EUREKA_PORT) || 8761,
            servicePath: "/eureka/apps/",
        },
    });

    eurekaClient.start((error?: Error) => {
        if (error) {
            console.log("EUREKA_REGISTRATION ERROR:", error);
        } else {
            console.log("Registered with Eureka");
        }
    });
});