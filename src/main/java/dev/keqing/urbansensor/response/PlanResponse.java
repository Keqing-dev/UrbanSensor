package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Plan;

public class PlanResponse {
    public static class PlanData extends DataResponse<Plan> {
        public PlanData(boolean status, Plan data) {
            super(status, data);
        }
    }
}
