
public class Task 
{
	int id;
	int C;
	int T;
	int D;
	int r;
	int preemptCount;
	int deadlineCount;
	int absDeadline;
	int priority;
	int deferrableServer;
	Task()
	{
		id = -1;
		C = -1;
		T = -1;
		D = -1;
		priority = -1;
		r = C;
		preemptCount = 0;
		deadlineCount = 0;
		absDeadline = 0;
		deferrableServer = 0;
	}
	Task(int id,int C,int T,int D)
	{
		this.id = id;
		this.C = C;
		this.T = T;
		this.D = D;
		r = C;
		priority = -1;
		preemptCount = 0;
		deadlineCount = 0;
		absDeadline = D;
		deferrableServer = 0;
	}

}
