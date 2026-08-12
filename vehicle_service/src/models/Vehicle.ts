import mongoose, { Schema, Document } from "mongoose";

export type VehicleType = "CAR" | "VAN" | "MOTORCYCLE" | "THREE_WHEELER" | "TRUCK" | "BUS";
export type VehicleStatus = "PARKED" | "EXITED";

export interface IVehicle extends Document {
    vehicleNumber: string;
    vehicleType: VehicleType;
    brand?: string;
    vehicleModel?: string;   // renamed from `model`
    color?: string;
    userId: string;
    status: VehicleStatus;
    lastEntryTime?: Date;
    lastExitTime?: Date;
    createdAt: Date;
    updatedAt: Date;
}

const vehicleSchema = new Schema<IVehicle>(
    {
        vehicleNumber: { type: String, required: true, unique: true, trim: true },
        vehicleType: {
            type: String,
            enum: ["CAR", "VAN", "MOTORCYCLE", "THREE_WHEELER", "TRUCK", "BUS"],
            required: true,
        },
        brand: { type: String },
        vehicleModel: { type: String },   // renamed from `model`
        color: { type: String },
        userId: { type: String, required: true },
        status: {
            type: String,
            enum: ["PARKED", "EXITED"],
            default: "EXITED",
        },
        lastEntryTime: { type: Date },
        lastExitTime: { type: Date },
    },
    { timestamps: true }
);

export default mongoose.model<IVehicle>("Vehicle", vehicleSchema);