package dropwlab.delaval.com.core.vc.listener;

import Delaval.VMSController.VCCache.CacheFunctionType;

public class EventList {

    public static CacheFunctionType[] getEventList() {
        return eventList;
    }

    /**
     * All the events that we are interested in.
     */
    private static final CacheFunctionType[] eventList = {
            CacheFunctionType.AllData,
            CacheFunctionType.Animal,
            CacheFunctionType.AnimalGroup,
            CacheFunctionType.AnimalLocation,
            CacheFunctionType.AnimalTrafficArea,
            CacheFunctionType.Alarm,
            CacheFunctionType.AmrBail,
            CacheFunctionType.AmrControlParam,
            CacheFunctionType.AmrDevice,
            CacheFunctionType.AmrHydraulics,
            CacheFunctionType.AmrRobot,
            CacheFunctionType.AmrRobotAllAxis,
            CacheFunctionType.AmrRobotAxis,
            CacheFunctionType.AmrRobotGeometry,
            CacheFunctionType.AmrTOFCamera,
            CacheFunctionType.AmsGateController,
            CacheFunctionType.AmsSubsystemController,
            CacheFunctionType.AnimalAndMilkings,
            CacheFunctionType.AnimalNoteEvent,
            CacheFunctionType.AnimalNoteEventType,
            CacheFunctionType.AnimalSelection,
            CacheFunctionType.DDMInfo,
            CacheFunctionType.WebCamera,
            CacheFunctionType.CodeSet,
            CacheFunctionType.DeviceEvent,
            CacheFunctionType.GatePassingEvent,
            CacheFunctionType.MilkQuality,
            CacheFunctionType.Farm,
            CacheFunctionType.Enums,
            CacheFunctionType.GateDecisionCriteria,
            CacheFunctionType.GateDirection,
            CacheFunctionType.MilkLoopCleaningAction,
            CacheFunctionType.MilkLoopCleaningEvent,
            CacheFunctionType.MilkLoopDevice,
            CacheFunctionType.MilkLoopCleaningDevice,
            CacheFunctionType.MilkLoopCleaningProgram,
            CacheFunctionType.MilkQualityAmr,
            CacheFunctionType.MilkTankDevice,
            CacheFunctionType.MilkTransportCleaningDevice,
            CacheFunctionType.OccSamplingSettings,
            CacheFunctionType.Performance,
            CacheFunctionType.SmartGate,
            CacheFunctionType.User,
            CacheFunctionType.VmsCleaningModuleConfiguration,
            CacheFunctionType.VmsCleaningModuleData,
            CacheFunctionType.VmsCleaningProgram,
            CacheFunctionType.VmsCleaningSummery,
            CacheFunctionType.VmsConfig,
            CacheFunctionType.VmsControllerDevice,
            CacheFunctionType.VmsMilking,
            CacheFunctionType.VmsMilkingStationDevice
    };
}
