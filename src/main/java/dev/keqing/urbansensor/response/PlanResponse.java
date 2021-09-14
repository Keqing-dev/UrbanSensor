package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Plan;

public class PlanResponse {
    public static class PlanData extends StatusResponse {
        private Plan data;

        public PlanData(boolean success, Plan data) {
            super(success);
            this.data = data;
        }

        public Plan getData() {
            return data;
        }

        public void setData(Plan data) {
            this.data = data;
        }
    }
}
