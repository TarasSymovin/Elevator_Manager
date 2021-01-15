package andriichello.scenes;

import andriichello.strategies.IElevatorChoosingStrategy;
import andriichello.strategies.IElevatorStrategy;
import andriichello.strategies.OwnershipElevatorStrategy;
import andriichello.strategies.SimpleElevatorChoosingStrategy;

public class ElevatorsSceneArgs {
    private int mFloorsCount = 5;
    private int mElevatorsCount = 3;
    private int mMaxPassengersCount = 4;
    private int mPassengersSpawnRate = 3000;
    private int mPassengersSpawnAmount = 1;
    private int mPassengersInitialCount = 0;

    private IElevatorChoosingStrategy mElevatorChoosingStrategy = new SimpleElevatorChoosingStrategy();
    private IElevatorStrategy mElevatorStrategy = new OwnershipElevatorStrategy();

    public ElevatorsSceneArgs() {
        
    }

    public int getFloorsCount() {
        return mFloorsCount;
    }

    public void setFloorsCount(int mFloorsCount) {
        this.mFloorsCount = mFloorsCount;
    }

    public int getElevatorsCount() {
        return mElevatorsCount;
    }

    public void setElevatorsCount(int mElevatorsCount) {
        this.mElevatorsCount = mElevatorsCount;
    }

    public int getMaxPassengersCount() {
        return mMaxPassengersCount;
    }

    public void setMaxPassengersCount(int mMaxPassengersCount) {
        this.mMaxPassengersCount = mMaxPassengersCount;
    }

    public int getPassengersSpawnRate() {
        return mPassengersSpawnRate;
    }

    public void setPassengersSpawnRate(int mPassengersSpawnRate) {
        this.mPassengersSpawnRate = mPassengersSpawnRate;
    }

    public int getPassengersSpawnAmount() {
        return mPassengersSpawnAmount;
    }

    public void setPassengersSpawnAmount(int mPassengersSpawnAmount) {
        this.mPassengersSpawnAmount = mPassengersSpawnAmount;
    }

    public int getPassengersInitialCount() {
        return mPassengersInitialCount;
    }

    public void setPassengersInitialCount(int mPassengersInitialCount) {
        this.mPassengersInitialCount = mPassengersInitialCount;
    }

    public IElevatorChoosingStrategy getElevatorChoosingStrategy() {
        return mElevatorChoosingStrategy;
    }

    public void setElevatorChoosingStrategy(IElevatorChoosingStrategy mElevatorChoosingStrategy) {
        this.mElevatorChoosingStrategy = mElevatorChoosingStrategy;
    }

    public IElevatorStrategy getElevatorStrategy() {
        return mElevatorStrategy;
    }

    public void setElevatorStrategy(IElevatorStrategy mElevatorStrategy) {
        this.mElevatorStrategy = mElevatorStrategy;
    }
}
