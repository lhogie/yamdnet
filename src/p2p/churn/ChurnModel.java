package p2p.churn;

public abstract class ChurnModel extends ChurnMessage
{

	public abstract void apply(Churn churn);

	public abstract double getRate();

}
