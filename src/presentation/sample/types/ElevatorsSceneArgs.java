package presentation.sample.types;

public class ElevatorsSceneArgs {
    public Integer floorsCount;
    public Integer elevatorsCount;
    public Integer strategyNumber;
    public Integer passengersCount;

    public ElevatorsSceneArgs(Integer floorsCount, Integer elevatorsCount, Integer strategyNumber, Integer passengersCount) {
        this.floorsCount = floorsCount;
        this.elevatorsCount = elevatorsCount;
        this.strategyNumber = strategyNumber;
        this.passengersCount = passengersCount;
    }
}
