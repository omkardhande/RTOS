import java.util.*;
import java.io.*;

public class RM_EDF 
{
	int RM[];
	RM_EDF(int n)
	{
		RM = new int[n];
	}
	public static void main(String[] args) throws Exception
	{
		FileReader f = new FileReader(".\\question1_taskset.txt");
		BufferedReader in = new BufferedReader(f);
		BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter 1 for RM, 2 for EDF other number to exit");
		int choice = Integer.parseInt(io.readLine());
		ArrayList<Task> t = new ArrayList<Task>();
		String s="";
		String sub; int counter = 0;int spaceCounter = 0;int numTasks = 0;
		int id = -1;
		int C = -1;
		int T = -1;
		int D = -1;
		int simT = 0;
		while((sub=in.readLine()) != null)
		{
			if(counter == 0)
			{
				numTasks = Integer.parseInt(sub);
			}
			else if(counter == numTasks+1)
			{
				simT = Integer.parseInt(sub.substring(sub.indexOf(' ') + 1));
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
							C = Integer.parseInt(s);
						}
						else if(spaceCounter == 3)
						{
							T = Integer.parseInt(s);
							D = Integer.parseInt(sub.substring(i+1));
							t.add(new Task(id,C,T,D));
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
		
		System.out.println("Parsed output: ");
		for(int i = 0;i< t.size();i++)
		{
			System.out.println(t.get(i).id+" "+t.get(i).C+" "+t.get(i).T+" "+t.get(i).D+" ");
		}
		RM_EDF ob = new RM_EDF(simT);
		
							/*-----Only for RM-----*/
		if(choice == 1)
		{
			System.out.println("Output written to : question1_RM_output.txt");
			PrintStream o = new PrintStream(new File("question1_RM_output.txt"));
			System.setOut(o);
			t = ob.assignPriorities(t);
			System.out.println("Output sorted according to priorities:");
			for(int i = 0;i< t.size();i++)
			{
				System.out.println(t.get(i).id+" "+t.get(i).C+" "+t.get(i).T+" "+t.get(i).D+" ");
			}
			ob.generateRMSchedule(t,simT);
			//System.out.println("According to RM");
			//System.out.println("Time\tTask");
			for(int i = 0;i< ob.RM.length;i++)
			{
				//System.out.println(i+"\t"+ob.RM[i]);
			}
			System.out.println("Task Summary according to priority:");
			for(int i = 0;i< t.size();i++)
			{
				System.out.println("----Task "+t.get(i).id+"----");
				System.out.println("Preempt count for task "+t.get(i).id+" is "+t.get(i).preemptCount);
				System.out.println("Deadline miss count for task "+t.get(i).id+" is "+t.get(i).deadlineCount);
			}
		}
		if(choice == 2)
		{
								/*-----Only for EDF-----*/
			System.out.println("Output written to : question1_EDF_output.txt");
			PrintStream o = new PrintStream(new File("question1_EDF_output.txt"));
			System.setOut(o);
			System.out.println("Parsed output: ");
			for(int i = 0;i< t.size();i++)
			{
				System.out.println(t.get(i).id+" "+t.get(i).C+" "+t.get(i).T+" "+t.get(i).D+" ");
			}
			ob.generateEDFSchedule(t, simT);
			//System.out.println("According to EDF");
			//System.out.println("Time\tTask");
			//for(int i = 0;i< ob.RM.length;i++)
			//{
			//	System.out.println(i+"\t"+ob.RM[i]);
			//}
			System.out.println("Task Summary according to priority:");
			for(int i = 0;i< t.size();i++)
			{
				System.out.println("----Task "+t.get(i).id+"----");
				System.out.println("Preempt count for task "+t.get(i).id+" is "+t.get(i).preemptCount);
				System.out.println("Deadline miss count for task "+t.get(i).id+" is "+t.get(i).deadlineCount);
			}
		}
	}
	ArrayList<Task> assignPriorities(ArrayList<Task> t)
	{
		Collections.sort(t,new MySort());
		return t;
	}
	
	void generateRMSchedule(ArrayList<Task> t,int simT)
	{
		for(int i=0;i<simT;i++)
		{
			if(i>0)
			{
				t = updateR(t,i);
			}
			if(i>1)
			{
				t = preempt(t,i);
			}

			for(int j =0;j<t.size();j++)
			{
				if(t.get(j).r!=0)
				{
					if(RM[i] == 0)
					{
						RM[i] = t.get(j).id;
						(t.get(j).r)--;
						System.out.println("Time = "+i+" Task = "+RM[i]);
						break;
					}
				}
			}
		}
		
	}
	ArrayList<Task> updateR(ArrayList<Task>t,int n)
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
					if(t.get(i).r != 0)
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
				if(t.get(i).r != 0 && (t.get(i).r != t.get(i).C))
				{
					System.out.println("Task "+t.get(i).id+" preempted at time "+(n-1));
					(t.get(i).preemptCount)++;
				}
			}
		}
		return t; 	
	}
	int maxPriority(ArrayList<Task> t, int n)
	{
		int ed = 0;
		int minVal = Integer.MAX_VALUE;
		for(int i = 0;i<t.size();i++)
		{
			if(Math.abs(t.get(i).D - n)< minVal && (t.get(i).r != 0))
			{
				ed = i;
				minVal = Math.abs(t.get(i).D - n);
			}
		}
		return ed;
	}
	void generateEDFSchedule(ArrayList<Task> t, int simT)
	{
		for(int i = 0;i<simT;i++)
		{
			if(i>0)
				t = updateD(t,i);
			if(i>1)
				t = preempt(t,i);
			int n = maxPriority(t,i);
			if(t.get(n).r != 0)
			{
				RM[i] = t.get(n).id;
				(t.get(n).r)--;
				System.out.println("Time = "+i+" Task = "+RM[i]);
			}
		}
	}
	ArrayList<Task> updateD(ArrayList<Task> t, int n)
	{
		for(int i = 0; i< t.size();i++)
		{
			if(t.get(i).T != t.get(i).absDeadline)
			{
				if(n%t.get(i).D == 0)
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

					t.get(i).D += t.get(i).T;
					t.get(i).r = t.get(i).C;
				}
			}
			else
			{
				if(n%t.get(i).T == 0)
				{
					if(t.get(i).r != 0)
					{
						System.out.println("Task "+t.get(i).id+" missed deadline at time "+n);
						(t.get(i).deadlineCount)++;
					}
					t.get(i).D += t.get(i).T;
					t.get(i).r = t.get(i).C;		
				}
			}
		}
		return t;
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
