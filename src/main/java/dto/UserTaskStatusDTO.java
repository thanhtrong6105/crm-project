package dto;

public class UserTaskStatusDTO {
    private double notStartedPercentage;
    private double inProgressPercentage;
    private double completedPercentage;

    public double getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(double completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

    public double getInProgressPercentage() {
        return inProgressPercentage;
    }

    public void setInProgressPercentage(double inProgressPercentage) {
        this.inProgressPercentage = inProgressPercentage;
    }

    public double getNotStartedPercentage() {
        return notStartedPercentage;
    }

    public void setNotStartedPercentage(double notStartedPercentage) {
        this.notStartedPercentage = notStartedPercentage;
    }

}
