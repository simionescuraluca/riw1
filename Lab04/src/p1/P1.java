package p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class P1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, HashMap<String, Integer>>> DirectIndex =directIndex();
		HashMap<String, HashMap<String, Integer>> IndirectIndex=indirectIndex(DirectIndex);
		String input=Read();
		String[] operatori_logici= {"AND", "OR", "NOT"};
		String[] read_words=Splitter(input);
		calc(read_words,operatori_logici,IndirectIndex);
	}
	public static HashMap<String, HashMap<String, Integer>> indirectIndex(ArrayList<HashMap<String,HashMap<String,Integer>>> list) throws IOException
	{
		HashMap<String,HashMap<String,Integer>> indiciIndirectiList=new HashMap<String,HashMap<String,Integer>> ();
		for(HashMap<String,HashMap<String,Integer>> h:list)
		{
			
			Iterator<?> it=h.entrySet().iterator();
			while(it.hasNext())
			{
				
				Map.Entry pair=(Map.Entry)it.next();
				String document=(String) pair.getKey();
				//System.out.println(pair.getKey() + " = " + pair.getValue());
				Iterator<?> it1=((HashMap<String, HashMap<String, Integer>>) pair.getValue()).entrySet().iterator();
				while(it1.hasNext())
				{
					Map.Entry pair1=(Map.Entry)it1.next();
					String cuvant=(String) pair1.getKey();
					Integer nrapar=(Integer) pair1.getValue();
					
					HashMap<String,Integer> dict=new HashMap<String,Integer>();
					dict.put(document, nrapar);
					
					if(!indiciIndirectiList.containsKey(cuvant))
						indiciIndirectiList.put(cuvant,dict);
					else 
					{
						HashMap<String,Integer> dict1=indiciIndirectiList.get(cuvant);
						dict1.put(document,nrapar);
						indiciIndirectiList.put(cuvant, dict1);
					}
				}
			}
		}
		//System.out.println(indiciIndirectiList.toString());
        return indiciIndirectiList;
	}
	public static ArrayList<HashMap<String, HashMap<String, Integer>>> directIndex() throws IOException
	{
		FileWalker fw=new FileWalker();
		ArrayList<HashMap<String,HashMap<String,Integer>>> list=new ArrayList<HashMap<String,HashMap<String,Integer>>>();
		//ArrayList <String> numeDocumente=new ArrayList<String>();
		String[] stopw={"are", "nu", "si"};
		List<File> fileList=new ArrayList<>();
		fw.walk("dir",fileList);
		for(int i=0;i<fileList.size();i++)
		{
			File file=fileList.get(i);
			
			System.out.println("File: " + file.getName());
			BufferedReader r=new BufferedReader(new FileReader(file));
	        String line=null;
	        HashMap<String,Integer> dict=new HashMap<String,Integer>();
	        while((line=r.readLine())!=null)
	        {
	        	String[] words=line.split("\\s+");
	        	for(String word:words)
	        	{
	        		if(!Arrays.stream(stopw).anyMatch(word::equals))
        			{
						if(dict.containsKey(word))
						{
							Integer val=dict.get(word);
							dict.put(word, val+1);
						}
						else
							dict.put(word, 1);
        			}
	        	}
	        	HashMap<String,HashMap<String,Integer>> document=new HashMap<String,HashMap<String,Integer>>();
	        	document.put(file.getName(), dict);
	        	
	        	String NewFileName="II"+" "+file.getName();
	        	FileWriter fwritter =new FileWriter(new File(NewFileName));
	        	fwritter.write(document.toString());
	        	fwritter.close();
	        	list.add(document);
	        }
	        
		}
		
		
		
		HashMap<String,HashMap<String,Integer>> indiciIndirectiList=indirectIndex(list);
		FileWriter fwritter =new FileWriter(new File("IndiciIndrecti.txt"));
		Iterator<?> it=indiciIndirectiList.entrySet().iterator();
		while(it.hasNext())
		{
	        Entry<?, ?> pair = (Entry<?, ?>)it.next();
	        fwritter.write(pair.getKey() + " = " + pair.getValue());
	        fwritter.write("\n");
	        it.remove();
		}
		fwritter.close();
		return list;
	}
	public static String Read() throws IOException
	{
		System.out.println("Introduceti propozitia logica:");
		BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
		String input =reader.readLine();
		return input;
	}
	public static String[] Splitter(String input)
	{
		String[] cuvinte_splitate=input.split("\\W+");
		return cuvinte_splitate;
	}
	public static ArrayList<String> CalcDocuments(ArrayList<String> doc_cuv1, ArrayList<String> doc_cuv2,ArrayList<String> doc_cuv3,String logical_operator)
	{
		//System.out.println(doc_cuv1);
		//System.out.println(doc_cuv2);
		//System.out.println(doc_cuv3);
		//System.out.println(logical_operator);
		ArrayList<String> doc_rez=new ArrayList<String>();
		if(doc_cuv3.isEmpty())
		{
			switch(logical_operator)
			{
				case "AND":
				{
					for(String d1:doc_cuv1)
					{
						for(String d2:doc_cuv2)
						{
							if(d1.equals(d2))
							{
								AddList(doc_cuv3,d1);
							}
						}
					}
					break;
				}
				case "OR":
				{
					for(String d1:doc_cuv1)
						AddList(doc_cuv3,d1);
					for(String d2:doc_cuv2)
						AddList(doc_cuv3,d2);
					break;
				}
				case "NOT":
				{
					for(String d1:doc_cuv1)
					{
						Boolean flag=false;
						for(String d2:doc_cuv2)
						{
							if(d1.equals(d2))
							{
								flag=true;
								break;
							}
						}
						if(flag==false)
							AddList(doc_cuv3,d1);
					}
					break;
				}
			}
			return doc_cuv3;
		}
		else
		{
			switch(logical_operator)
			{
				case "AND":
				{
					for(String d1:doc_cuv3)
					{
						for(String d2:doc_cuv2)
						{
							if(d1.equals(d2))
							{
								AddList(doc_rez,d1);
							}
						}
					}
					break;
				}
				case "OR":
				{
					for(String d1:doc_cuv3)
						AddList(doc_rez,d1);
					for(String d2:doc_cuv2)
						AddList(doc_rez,d2);
					break;
				}
				case "NOT":
				{
					for(String d1:doc_cuv3)
					{
						Boolean flag=false;
						for(String d2:doc_cuv2)
						{
							if(d1.equals(d2))
							{
								flag=true;
								break;
							}
						}
						if(flag==false)
							AddList(doc_rez,d1);
					}
					break;
				}
			}
		}
		return doc_rez;
	}
	public static void calc(String[] read_words,String[] operatori_logici,HashMap<String, HashMap<String, Integer>> IndirectIndex)
	{
		String logical_operator=new String();
		
		String cuv1=new String(),cuv2=new String();
		ArrayList<String> doc_cuv1=new ArrayList<String>(), doc_cuv2 = new ArrayList<String>();
		ArrayList<String> doc_rez=new ArrayList<String>();
		for(int i=0;i<read_words.length;i++)
		{
			Boolean logical_operator_flag=false;
			for(int j=0;j<operatori_logici.length;j++)
			{
				if(read_words[i].equals(operatori_logici[j]))
				{
					logical_operator=read_words[i];
					logical_operator_flag=true;
					break;
				}
			}
			if(logical_operator_flag==true)
				continue;
			else
			{
				if(i==0)
				{
					continue;
				}
				else
				{
					cuv1=read_words[i-2];
					cuv2=read_words[i];
					doc_cuv1=new ArrayList<String>();
					doc_cuv2 = new ArrayList<String>();
					Iterator<?> it=IndirectIndex.entrySet().iterator();
					while(it.hasNext())
					{
						Map.Entry pair=(Map.Entry)it.next();
						String cuvant=(String) pair.getKey();

						if(cuvant.equals(cuv1))
						{
							
							Iterator<?> it1=((HashMap<java.lang.String, HashMap<java.lang.String, Integer>>) pair.getValue()).entrySet().iterator();
							while(it1.hasNext())
							{
								Map.Entry pair1=(Map.Entry)it1.next();
								String doc=(String) pair1.getKey();
								doc_cuv1.add(doc);
							}
						}
						if(cuvant.equals(cuv2))
						{
							Iterator<?> it1=((HashMap<java.lang.String, HashMap<java.lang.String, Integer>>) pair.getValue()).entrySet().iterator();
							while(it1.hasNext())
							{
								Map.Entry pair1=(Map.Entry)it1.next();
								String doc=(String) pair1.getKey();
								doc_cuv2.add(doc);
							}
						}
					}
				}
			}
			doc_rez=CalcDocuments(doc_cuv1,doc_cuv2,doc_rez,logical_operator);
		}
		System.out.println(doc_rez);
	}
	public static void AddList(ArrayList<String> list,String s)
	{
		if(!list.contains(s))
			list.add(s);
	}
}
