
public class Aperiodic_Task 
{
	int id;
	int arrivalTime;
	int C;
	int initC;
	int respTime;
	Aperiodic_Task(int id,int arrivalTime,int C)
	{
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.C = C;
		initC = C;
		respTime = -1;
	}
}
