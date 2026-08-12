import { Router } from "express";
import {
    saveVehicle,
    getVehicle,
    getAllVehicles,
    getVehiclesByUser,
    updateVehicle,
    deleteVehicle,
    recordEntry,
    recordExit,
} from "../controllers/vehicle.controller";

const router = Router();

router.post("/", saveVehicle);
router.get("/", getAllVehicles);
router.get("/user/:userId", getVehiclesByUser);
router.get("/:id", getVehicle);
router.put("/:id", updateVehicle);
router.delete("/:id", deleteVehicle);
router.post("/:id/entry", recordEntry);
router.post("/:id/exit", recordExit);

export default router;