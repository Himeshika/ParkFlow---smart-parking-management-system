import { Request, Response } from "express";
import Vehicle from "../models/Vehicle";

export const saveVehicle = async (req: Request, res: Response) => {
    try {
        const { vehicleNumber, vehicleType, brand, vehicleModel, color, userId } = req.body;

        if (!vehicleNumber) {
            return res.status(400).json({ message: "Vehicle number is required" });
        }
        if (!vehicleType) {
            return res.status(400).json({ message: "Vehicle type is required" });
        }
        if (!userId) {
            return res.status(400).json({ message: "User ID is required" });
        }

        const existing = await Vehicle.findOne({ vehicleNumber });
        if (existing) {
            return res.status(409).json({ message: "Vehicle number already registered" });
        }

        const vehicle = await Vehicle.create({
            vehicleNumber,
            vehicleType,
            brand,
            vehicleModel,
            color,
            userId,
            status: "EXITED",
        });

        return res.status(201).json({ message: "Vehicle registered successfully", data: vehicle });
    } catch (error) {
        console.log("SAVE_VEHICLE ERROR:", error);
        return res.status(500).json({ message: "Something went wrong", error });
    }
};

export const getVehicle = async (req: Request, res: Response) => {
    try {
        const { id } = req.params;
        const vehicle = await Vehicle.findById(id);

        if (!vehicle) {
            return res.status(404).json({ message: "Vehicle not found" });
        }

        return res.status(200).json({ message: "Vehicle retrieved successfully", data: vehicle });
    } catch (error) {
        console.log("GET_VEHICLE ERROR:", error);
        return res.status(500).json({ message: "Something went wrong", error });
    }
};

export const getAllVehicles = async (req: Request, res: Response) => {
    try {
        const vehicles = await Vehicle.find();
        return res.status(200).json({ message: "Vehicle list retrieved successfully", data: vehicles });
    } catch (error) {
        console.log("GET_ALL_VEHICLES ERROR:", error);
        return res.status(500).json({ message: "Something went wrong", error });
    }
};

export const getVehiclesByUser = async (req: Request, res: Response) => {
    try {
        const { userId } = req.params;
        const vehicles = await Vehicle.find({ userId });
        return res.status(200).json({ message: "Vehicles retrieved successfully", data: vehicles });
    } catch (error) {
        console.log("GET_VEHICLES_BY_USER ERROR:", error);
        return res.status(500).json({ message: "Something went wrong", error });
    }
};

export const updateVehicle = async (req: Request, res: Response) => {
    try {
        const { id } = req.params;
        const { vehicleType, brand, vehicleModel, color, userId } = req.body;

        if (!vehicleType) {
            return res.status(400).json({ message: "Vehicle type is required" });
        }
        if (!userId) {
            return res.status(400).json({ message: "User ID is required" });
        }

        const vehicle = await Vehicle.findById(id);
        if (!vehicle) {
            return res.status(404).json({ message: "Vehicle not found" });
        }

        vehicle.vehicleType = vehicleType;
        vehicle.brand = brand;
        vehicle.vehicleModel = vehicleModel;
        vehicle.color = color;
        vehicle.userId = userId;

        await vehicle.save();

        return res.status(200).json({ message: "Vehicle updated successfully", data: vehicle });
    } catch (error) {
        console.log("UPDATE_VEHICLE ERROR:", error);
        return res.status(500).json({ message: "Something went wrong", error });
    }
};

export const deleteVehicle = async (req: Request, res: Response) => {
    try {
        const { id } = req.params;
        const vehicle = await Vehicle.findById(id);

        if (!vehicle) {
            return res.status(404).json({ message: "Vehicle not found" });
        }

        await vehicle.deleteOne();

        return res.status(200).json({ message: "Vehicle deleted successfully" });
    } catch (error) {
        console.log("DELETE_VEHICLE ERROR:", error);
        return res.status(500).json({ message: "Something went wrong", error });
    }
};

export const recordEntry = async (req: Request, res: Response) => {
    try {
        const { id } = req.params;
        const vehicle = await Vehicle.findById(id);

        if (!vehicle) {
            return res.status(404).json({ message: "Vehicle not found" });
        }

        if (vehicle.status === "PARKED") {
            return res.status(400).json({ message: "Vehicle is already marked as parked" });
        }

        vehicle.status = "PARKED";
        vehicle.lastEntryTime = new Date();
        await vehicle.save();

        return res.status(200).json({ message: "Vehicle entry recorded", data: vehicle });
    } catch (error) {
        console.log("RECORD_ENTRY ERROR:", error);
        return res.status(500).json({ message: "Something went wrong", error });
    }
};

export const recordExit = async (req: Request, res: Response) => {
    try {
        const { id } = req.params;
        const vehicle = await Vehicle.findById(id);

        if (!vehicle) {
            return res.status(404).json({ message: "Vehicle not found" });
        }

        if (vehicle.status === "EXITED") {
            return res.status(400).json({ message: "Vehicle has already exited" });
        }

        vehicle.status = "EXITED";
        vehicle.lastExitTime = new Date();
        await vehicle.save();

        return res.status(200).json({ message: "Vehicle exit recorded", data: vehicle });
    } catch (error) {
        console.log("RECORD_EXIT ERROR:", error);
        return res.status(500).json({ message: "Something went wrong", error });
    }
};