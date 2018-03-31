import java.io.*;
import java.util.*;


public class RM_DS 
{
	int RM[];
	RM_DS(int n)
	{
		RM = new int[n];
	}
	public static void main(String[] args) throws Exception
	{
		FileReader f = new FileReader(".\\question2_taskset.txt");
		BufferedReader in = new BufferedReader(f);
		System.out.println("Output written to : question2_output.txt");
		PrintStream o = new PrintStream(new File("question2_output.txt"));
		System.setOut(o);
		ArrayList<Task> t = new ArrayList<Task>();
		ArrayList<Aperiodic_Task> a = new ArrayList<Aperiodic_Task>();
		String s="";
		String sub; int counter = 0;int spaceCounter = 0;int numTasks = 0;
		int id = -1;
		int C = -1;
		int T = -1;
		int D = -1;
		int DS = -1;
		int simT = 0;
		int numATasks = 0;
		int arrivalTime  = 0;
		while((sub=in.readLine()) != null)
		{
			if(counter == 0)
			{
				numTasks = Integer.parseInt(sub);
			}
			else if(counter == numTasks +1)
			{

			}
			else if(counter == numTasks +2)
			{
				numATasks = Integer.parseInt(sub);
			}
			else if(counter == numTasks + numATasks + 3)
			{
				simT = Integer.parseInt(sub.substring(sub.indexOf(' ') + 1));
			}
			else if(counter > 0 && counter <= numTasks)
			{
				for(int i = 0;i<sub.length();i++)
				{
					if(sub.charAt(i) == ' ')
					{
						spaceCounter++;
						if(spaceCounter == 1)
						{
							id = Integer.parseInt(s);
						}
						else if(spaceCounter == 2)
						{
							C = Integer.parseInt(s);
						}
						else if(spaceCounter == 3)
						{
							T = Integer.parseInt(s);
						}
						else if(spaceCounter == 4)
						{
							D = Integer.parseInt(s);
							DS = Integer.parseInt(sub.substring(i+1));
							t.add(new Task(id,C,T,D));
							t.get(t.size()-1).deferrableServer = DS;
							spaceCounter = 0;
							s = "";
							break;
						}
						else
						{
							continue;
						}
						s = "";
					}
					else
					{
						s = s+sub.charAt(i);
					}
				}
			}
			else
			{
				for(int i = 0;i<sub.length();i++)
				{
					if(sub.charAt(i) == ' ')
					{
						spaceCounter++;
						if(spaceCounter == 1)
						{
							id = Integer.parseInt(s);
						}
						else if(spaceCounter == 2)
						{
							arrivalTime = Integer.parseInt(s);
							C = Integer.parseInt(sub.substring(i+1));
							a.add(new Aperiodic_Task(id,arrivalTime,C));
							spaceCounter = 0;
							s = "";
							break;
						}
						else
						{
							continue;
						}
						s = "";
					}
					else
					{
						s = s+sub.charAt(i);
					}
				}
			}
			counter++;
		}
		f.close();	
		
		System.out.println("Parsed output- ");
		System.out.println("Periodic tasks:");
		for(int i = 0;i< t.size();i++)
		{
			System.out.println(t.get(i).id+" "+t.get(i).C+" "+t.get(i).T+" "+t.get(i).D+" ");
		}
		System.out.println("Aperiodic tasks:");
		for(int i = 0;i< a.size();i++)
		{
			System.out.println(a.get(i).id+" "+a.get(i).arrivalTime+" "+a.get(i).C);
		}
		RM_DS ob = new RM_DS(simT);
		t = ob.assignPriorities(t);
		a = ob.assignPrioritiesA(a);
		System.out.println("Output sorted according to priorities- ");
		System.out.println("Periodic tasks:");
		for(int i = 0;i< t.size();i++)
		{
			System.out.println(t.get(i).id+" "+t.get(i).C+" "+t.get(i).T+" "+t.get(i).D+" ");
		}
		System.out.println("Output sorted according to arrival times- ");
		System.out.println("Aperiodic tasks:");
		for(int i = 0;i< a.size();i++)
		{
			System.out.println(a.get(i).id+" "+a.get(i).arrivalTime+" "+a.get(i).C);
		}
		ob.generateRMSchedule(numTasks,t,simT,a);
		/*System.out.println("According to RM");
		System.out.println("Time\tTask");
		for(int i = 0;i< ob.RM.length;i++)
		{
			System.out.println(i+"\t"+ob.RM[i]);
		}*/
		System.out.println("Task Summary according to priority:");
		for(int i = 0;i< t.size();i++)
		{
			System.out.println("----Task "+t.get(i).id+"----");
			System.out.println("Preempt count for task "+t.get(i).id+" is "+t.get(i).preemptCount);
			System.out.println("Deadline miss count for task "+t.get(i).id+" is "+t.get(i).deadlineCount);
		}
	}
	ArrayList<Task> assignPriorities(ArrayList<Task> t)
	{
		Collections.sort(t,new MySort());
		return t;
	}
	void generateRMSchedule(int numTasks,ArrayList<Task> t,int simT,ArrayList<Aperiodic_Task> a)
	{
		int totalRespTime = 0;
		int nTasks = a.size();
		float avgRespTime = 0.0f;
		
		for(int i=0;i<simT;i++)
		{
			if(i>0)
			{
				t = updateR(numTasks,t,i);
			}
			if(i>1)
			{
				t = preempt(t,i);
			}

			for(int j =0;j<t.size();j++)
			{
				if(t.get(j).deferrableServer == 1)
				{
					if(a.size()>0 && t.get(j).r > 0)
					{
						Aperiodic_Task a1 = a.get(0);
						if(a1.C > 0 && i>=a1.arrivalTime)
						{
							RM[i] = t.get(j).id;
							System.out.println("Time = "+i+" Task = "+RM[i]);
							(a.get(0).C)--;
							if(a.get(0).C == 0)
							{
								totalRespTime += (i - a1.arrivalTime + 1);
								System.out.println("Aperiodic task "+a1.id+" completed at time "+(i)+" with response time "+ (i - a1.arrivalTime + 1));
								a.remove(0);
							}
							(t.get(j).r)--;
							break;
						}
					}
				}
				else if(t.get(j).r!=0)
				{
					if(RM[i] == 0)
					{
						RM[i] = t.get(j).id;
						System.out.println("Time = "+i+" Task = "+RM[i]);
						(t.get(j).r)--;
						break;
					}
				}
			}
		}
		avgRespTime = (float)totalRespTime/(float)nTasks;
		System.out.println("Average response time for aperiodic tasks is "+avgRespTime);
	}
	ArrayList<Task> updateR(int numTasks,ArrayList<Task>t,int n)
	{
		for(int i = 0; i< t.size();i++)
		{
			if(t.get(i).T != t.get(i).D)
			{
				if(n == (t.get(i).D + ((n/t.get(i).T)*t.get(i).T)) && n%t.get(i).T != 0)
				{
					if(t.get(i).r != 0)
					{
						System.out.println("Task "+t.get(i).id+" missed deadline at time "+n);
						(t.get(i).deadlineCount)++;
						t.get(i).r = 0;
					}
				}
				else if(n%t.get(i).T == 0)
				{
					if(t.get(i).r != 0)
					{
						System.out.println("Task "+t.get(i).id+" missed deadline at time "+n);
						(t.get(i).deadlineCount)++;
					}
					t.get(i).r = t.get(i).C;
				}
			}
			else
			{
				if(n%t.get(i).T == 0)
				{
					if(t.get(i).r != 0 && t.get(i).deferrableServer == 0)
					{
						System.out.println("Task "+t.get(i).id+" missed deadline at time "+n);
						(t.get(i).deadlineCount)++;
					}
					t.get(i).r = t.get(i).C;
				}
			}
		}
		return t;
	}
	ArrayList<Task> preempt(ArrayList<Task>t,int n)
	{
		int i=0;
		if((RM[n-1] != RM[n-2]) && RM[n-1]!=0 && RM[n-2]!=0)
		{
			for(i = 0; i< t.size();i++)
			{
				if(t.get(i).id == RM[n-2])
				{
					break;
				}
			}
			if(i < t.size())
			{
				if(t.get(i).r != 0 && t.get(i).deferrableServer == 0)
				{
					System.out.println("Task "+t.get(i).id+" preempted at time "+(n-1));
					(t.get(i).preemptCount)++;
				}
			}
		}
		return t; 	
	}
	ArrayList<Aperiodic_Task> assignPrioritiesA(ArrayList<Aperiodic_Task> a)
	{
		Collections.sort(a,new MySortA());
		return a;
	}
}

class MySortA implements Comparator<Aperiodic_Task>{
	 
    public int compare(Aperiodic_Task e1, Aperiodic_Task e2) {
        if(e1.arrivalTime > e2.arrivalTime){
            return 1;
        } else {
            return -1;
        }
    }
}
class MySort implements Comparator<Task>{
	 
    public int compare(Task e1, Task e2) {
        if(e1.T > e2.T){
            return 1;
        } else {
            return -1;
        }
    }
}
