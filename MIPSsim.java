import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MIPSsim {

	String outfile;
	StringBuilder midfile;
	String addfile;
	int currentLine;

	String inQue;
	Queue preQueue = new LinkedList();
	Queue<String> instructionQueue = new LinkedList<String>();

	
	static reservationStation res;

	MIPSsim() {
		outfile = null;
		midfile = new StringBuilder();
		addfile = null;

	}

	public static void main(String[] inputArguments) throws IOException {

		// Input Arguments = java MIPSsim InputFile OutputFile [-Tm:n]
		ouputFile = inputArguments[1];
		if (inputArguments[0] != (null) && inputArguments[1] != (null) && inputArguments.length == 2)
		// checking the input arguments

		// decoding and printing functions called
		{
			MIPSsim msim = new MIPSsim();
			String nextInput = msim.instructionDisassembler(inputArguments[0]);
			String[] rows = nextInput.split("\n");

			msim.simulate(rows);

		}

		else
			System.out.println("Invalid Arguments");

	}

	public int convertBinaryToDecimal(String str, int b) {
		int decimal;
		decimal = 0;
		decimal = Integer.parseInt(str, b);
		return decimal;

	}

	public String instructionDisassembler(String infile) throws IOException

	{
		// function to disassemble instruction
		FileInputStream fi = new FileInputStream(infile);
		byte[] bytestreamdata = new byte[4];
		int bytecounter = 4;
		boolean stoppoint = false;
		String[] instrpartition;
		while (!stoppoint) {
			bytecounter = fi.read(bytestreamdata);

			if (bytecounter < 4) {
				System.out.println("Word is not complete");
				fi.close();
				return "error";
			}

			String bytes = "";

			for (int i = 0; i < 4; i++) {

				int unsigned = bytestreamdata[i] & 0xFF;
				String bin = Integer.toBinaryString(unsigned);

				while (bin.length() < 8) {

					StringBuffer sb = new StringBuffer(bin);
					sb.insert(0, "0");
					bin = sb.toString();

				}

				bytes += bin;

			}

			instrpartition = new String[6];
			instrpartition[0] = bytes.substring(0, 6);
			instrpartition[1] = bytes.substring(6, 11);
			instrpartition[2] = bytes.substring(11, 16);
			instrpartition[3] = bytes.substring(16, 21);
			instrpartition[4] = bytes.substring(21, 26);
			instrpartition[5] = bytes.substring(26);

			stoppoint = disinst(instrpartition);
		}

		fi.close();
		return midfile.toString();
	}

	public boolean disinst(String[] instrpartition) throws FileNotFoundException, UnsupportedEncodingException {
		boolean stoppoint = false;
		int regs, regt, regd = 0;
		String immediate;
		int opcode = convertBinaryToDecimal(instrpartition[0], 2);
		switch (opcode) {

		case 0:

			if (instrpartition[5].equals("001101")) {
				stoppoint = true;
				midfile.append("BREAK \n");
			}
			if (instrpartition[5].equals("000000") && instrpartition[2].equals("00000")) {
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				if (regd == 0)
					midfile.append("NOP \n");
			}
			if (instrpartition[5].equals("100001")) {
				midfile.append("ADDU ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}
			if (instrpartition[5].equals("100000"))

			{
				midfile.append("ADD ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}
			if (instrpartition[5].equals("100100")) {
				midfile.append("AND ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}
			if (instrpartition[5].equals("100101")) {
				midfile.append("OR ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}
			if (instrpartition[5].equals("100110")) {
				midfile.append("XOR ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}
			if (instrpartition[5].equals("100111")) {
				midfile.append("NOR ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}
			if (instrpartition[5].equals("100010")) {
				midfile.append("SUB ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}
			if (instrpartition[5].equals("100011")) {
				midfile.append("SUBU ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}

			if (instrpartition[5].equals("000000")) {

				regs = convertBinaryToDecimal(instrpartition[4], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				if (regt != 0) {
					midfile.append("SLL ");
					midfile.append("R" + regd + ", R" + regt + ", #" + regs + "\n");
				}
			}
			if (instrpartition[5].equals("000010")) {
				midfile.append("SRL ");
				regs = convertBinaryToDecimal(instrpartition[4], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regt + ", #" + regs + "\n");
			}
			if (instrpartition[5].equals("000011")) {
				midfile.append("SRA ");
				regs = convertBinaryToDecimal(instrpartition[4], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regt + ", #" + regs + "\n");
			}
			if (instrpartition[5].equals("101010")) {
				midfile.append("SLT ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}
			if (instrpartition[5].equals("101011")) {
				midfile.append("SLTU ");
				regs = convertBinaryToDecimal(instrpartition[1], 2);
				regt = convertBinaryToDecimal(instrpartition[2], 2);
				regd = convertBinaryToDecimal(instrpartition[3], 2);
				midfile.append("R" + regd + ", R" + regs + ", R" + regt + "\n");
			}

			break;

		case 8:
			midfile.append("ADDI ");
			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];
			regd = convertBinaryToDecimal(immediate, 2);
			if (immediate.startsWith("1"))
				regd = regd - 65536;
			midfile.append("R" + regt + ", R" + regs + ", #" + regd + "\n");
			break;

		case 10:
			midfile.append("SLTI ");
			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];
			regd = convertBinaryToDecimal(immediate, 2);
			if (immediate.startsWith("1"))
				regd = regd - 65536;
			midfile.append("R" + regt + ", R" + regs + ", #" + regd + "\n");
			break;

		case 9:
			midfile.append("ADDIU ");
			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];
			regd = convertBinaryToDecimal(immediate, 2);
			if (immediate.startsWith("1"))
				regd = regd - 65536;
			midfile.append("R" + regt + ", R" + regs + ", #" + regd + "\n");
			break;
		case 4:
			midfile.append("BEQ ");
			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];
			immediate += "00";
			midfile.append("R" + regs + ", R" + regt + ", #" + convertBinaryToDecimal(immediate, 2) + "\n");
			break;
		case 5:
			midfile.append("BNE ");
			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];
			immediate += "00";
			midfile.append("R" + regs + ", R" + regt + ", #" + convertBinaryToDecimal(immediate, 2) + "\n");
			break;
		case 1:

			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];
			if (regt == 1) {
				immediate += "00";
				midfile.append("BGEZ ");
				midfile.append("R" + regs + ", #" + convertBinaryToDecimal(immediate, 2) + "\n");
			}
			if (regt == 0) {
				immediate += "00";
				midfile.append("BLTZ ");
				midfile.append("R" + regs + ", #" + convertBinaryToDecimal(immediate, 2) + "\n");
			}
			break;
		case 6:

			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];

			immediate += "00";
			midfile.append("BLEZ ");
			if (immediate.startsWith("0"))
				midfile.append("R" + regs + ", #" + convertBinaryToDecimal(immediate, 2) + "\n");
			else
				midfile.append("R" + regs + ", #" + (convertBinaryToDecimal(immediate, 2) - 262144) + "\n");

			break;

		case 7:

			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];

			immediate += "00";
			midfile.append("BGTZ ");
			midfile.append("R" + regs + ", #" + convertBinaryToDecimal(immediate, 2) + "\n");

			break;
		case 2:
			midfile.append("J #");

			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[1] + instrpartition[2] + instrpartition[3] + instrpartition[4]
					+ instrpartition[5];
			immediate += "00";
			midfile.append(convertBinaryToDecimal(immediate, 2) + "\n");
			break;
		case 43:
			midfile.append("SW ");
			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];

			midfile.append("R" + regt + ", " + convertBinaryToDecimal(immediate, 2) + "(R" + regs + ") \n");
			break;
		case 35:
			midfile.append("LW ");
			regs = convertBinaryToDecimal(instrpartition[1], 2);
			regt = convertBinaryToDecimal(instrpartition[2], 2);
			immediate = instrpartition[3] + instrpartition[4] + instrpartition[5];

			midfile.append("R" + regt + ", " + convertBinaryToDecimal(immediate, 2) + "(R" + regs + ") \n");
			break;

		default:
			getWriter().println("Opcode not Found");
			break;
		}

		if (instrpartition[0].equals("000000") && instrpartition[5].equals("001101")) {
			stoppoint = true;
		}
		return stoppoint;

	}

	public void simulate(String[] rows) throws FileNotFoundException, UnsupportedEncodingException
	{
		int cycle = 1;
		String store1= null;
		String store2= null;
		String fetch = null;
		String decode = null;
		String execute = null;
		String writeBack = null;
		String commit = null;
		boolean flag;
		int programCounter = 600;
		int size = rows.length;
        ArrayList<Integer> progCounter= null;
		ArrayList<String> rs = null;
		ArrayList<String> rob = null;
		int[] registers = new int[32];
		ArrayList<String> btb = null ;
		int[] dataSegment = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };


		for (int j = 0; j < rows.length; j++)
		// for (String row : rows)
		{
			 
			 res = new reservationStation();
			 if (progCounter == null) 
				{
					progCounter = new ArrayList<Integer>();
				}
			     progCounter.add(programCounter);
			if (cycle == 1) {
				fetch = rows[j];
				getWriter().println("Cycle <1>: \nIQ: \n[" + rows[j] + "] \nRS: \nROB:");
				getWriter().println("BTB: \nREGISTERS:");
				getWriter().print("R00:");
				for (int i = 0; i < 8; i++) {
					getWriter().print(registers[i] + "\t ");
				}
				getWriter().print("\nR08:");
				for (int i = 8; i < 16; i++) {
					getWriter().print(registers[i] + "\t ");
				}
				getWriter().print("\nR16:");
				for (int i = 16; i < 24; i++) {
					getWriter().print(registers[i] + "\t ");
				}
				getWriter().print("\nR24:");
				for (int i = 24; i < 32; i++) {
					getWriter().print(registers[i] + "\t ");
				}
				getWriter().println("\nData Segment:");
				getWriter().print("716 \t ");
				for (int i = 0; i < dataSegment.length; i++) {
					getWriter().print(dataSegment[i] + "\t ");
				}

			} else
			{
				if (rob == null)
				{
					rob = new ArrayList<String>();
				}
				if (rs == null) 
				{
					rs = new ArrayList<String>();
				}
				rob.add(rows[j - 1]);
				rs.add(rows[j - 1]);
				
				// when you need to write to outputfile do
				// getWriter().println(variable name);
				String[] operation = fetch.split(" ");
				String opcode = operation[0];
				if (opcode.equals("SW") || opcode.equals("LW"))
				{
				commit = execute;
				execute = store1;
				store1 = writeBack;
				writeBack = decode;
				decode = fetch;
				fetch = rows[j];
				}
				else 
				{
					commit = execute;
					execute = store2;
					store2 = writeBack;
					writeBack = decode;
					decode = fetch;
					fetch = rows[j];
				}
				
				ifetch(cycle);
				/*
				 * System.out.println("fetch"+fetch);
				 * System.out.println("decode"+decode);
				 * System.out.println("execute"+execute);
				 * System.out.println("writeback"+writeBack);
				 * System.out.println("commit"+commit);
				 */
				if (opcode =="BREAK")
				{
					break;
				}
				else{
				idecode(rows[j]);
				}
				if (execute != null) 
				{
				 	programCounter =iexecute(rows, j,execute, cycle, registers, dataSegment, programCounter, res, progCounter);

				}
				//String[] operation = fetch.split(" ");
				//String opcode = operation[0];

				if (opcode.equals("SW") || opcode.equals("LW")) {
					icommit(execute, rs, rob, opcode, cycle);
				} else {
					iwriteBack(opcode);
					icommit(execute, rs, rob, opcode, cycle);
				}

				getWriter().println("RS: \n");
					for (int i = 0; i < rs.size(); i++) {
						getWriter().println("[" + (rs.get(i)).toString() + "]\n");
				    }
					getWriter().println("ROB: \n");
					for (int i = 0; i < rob.size(); i++) {
						getWriter().println("[" + (rob.get(i)).toString() + "]\n");
				    }
					
					if(opcode == "BEQ" || opcode == "BNE" || opcode == "BGEZ"||opcode == "BGTZ" || opcode == "BLEZ" || opcode == "BLTZ")
					{
						btb.add(rows[j]);
					}
						if(btb != null)
						{
						for (int u = 0; u < btb.size(); u++) 
						{
						getWriter().println("[ENTRY" + u + "]:<");
						getWriter().println((btb).get(u) + ">\n");
				        }

						}
						else
						{
							getWriter().println("BTB:");
						}
					
					getWriter().print("REGISTERS:\nR00:");
					for (int a = 0; a < 8; a++) {
						getWriter().print(registers[a] + "\t ");
					}
					getWriter().print("\nR08:");
					for (int b = 8; b < 16; b++) {
						getWriter().print(registers[b] + "\t ");
					}
					getWriter().print("\nR16:");
					for (int c = 16; c < 24; c++) {
						getWriter().print(registers[c] + "\t ");
					}
					getWriter().print("\nR24:");
					for (int d = 24; d < 32; d++) {
						getWriter().print(registers[d] + "\t ");
					}
					getWriter().println("\nData Segment:");
					getWriter().print("716 \t");
					for (int e = 0; e < dataSegment.length; e++) {
						getWriter().print(dataSegment[e] + "\t ");
					}
				}// end of else statement
			cycle++;
			
			} // end of for loop
		getWriter().close();

		} // end of simulate method

	

	

	private static PrintWriter getWriter() throws FileNotFoundException, UnsupportedEncodingException {
		if (writer == null) {
			writer = new PrintWriter(ouputFile, "UTF-8");
		}
		return writer;
	}

	public void ifetch(int cycle) throws FileNotFoundException, UnsupportedEncodingException {

		getWriter().println("\nCycle <" + cycle + ">:");

	}

	public void idecode(String row) throws FileNotFoundException, UnsupportedEncodingException {

		getWriter().println("IQ: \n[" + row + "]");

	}

//	rows, j,execute, cycle, registers, dataSegment, programCounter, res, progCounter
	public int iexecute(String[] rows, int j, String execute, int cycle, int[] registers, int[] dataSegment, int programCounter, reservationStation res, ArrayList progCounter)
	{
		String[] operation = execute.split(" ");
		String opcode = operation[0];
		
		if (opcode.equals("SW") || opcode.equals("LW"))

		{
			String op1 = operation[1];
			String op2 = operation[2];
			String[] oper1 = op1.split("R");
			int operand1 = Integer.parseInt((oper1[1].split(","))[0]);// has
																		// first
																		// part
																		// register
																		// number
			String[] addr = op2.split("\\(");
			String dataaddr;

			if (opcode.equals("LW")) {
				dataaddr = addr[0];
				//int dataaddd = Integer.parseInt(dataaddr);// datasegment address
				res.vk= Integer.parseInt(dataaddr);
				//int dataadd = (res.vk - 716) / 4;
				String regaddr = addr[1];
				

				String[] address = regaddr.split("\\)");

				String[] opertn1 = address[0].split("R");
				int sourceadd = Integer.parseInt(opertn1[1]);// has destination
																// register
																// number
				res.ad = registers[sourceadd];
				//registers[operand1] = dataSegment[dataadd + registers[sourceadd]];
				//res.vj= dataSegment[dataadd + res.ad];
				
				
				
				registers[operand1]= dataSegment[((res.vk + res.ad)-716)/4];
				
				programCounter = programCounter +4;
		

			} // if load instruction

			else {
				dataaddr = addr[0];
				//int dataaddd = Integer.parseInt(dataaddr);// datasegment address
				res.vk= Integer.parseInt(dataaddr);
				//int dataadd = (res.vk - 716) / 4;
				String regaddr = addr[1];
				

				String[] address = regaddr.split("\\)");

				String[] opertn1 = address[0].split("R");
				int sourceadd = Integer.parseInt(opertn1[1]);// has destination
																// register
																// number
				res.ad = registers[sourceadd];
				//registers[operand1] = dataSegment[dataadd + registers[sourceadd]];
				//res.vj= dataSegment[dataadd + res.ad];
				
				
				
				 dataSegment[((res.vk + res.ad)-716)/4]= registers[operand1];
				
				 programCounter = programCounter +4;
				
				

			} // if store instruction

		} // end of outer if for load store instructions

		else if (opcode.equals("NOP")) 
		{
			cycle++;
			programCounter = programCounter +4;
			
			
		} // end of else if for nop

		else if (opcode.equals("BREAK")) 
		{
          
		} // end of else if for break

		else if (opcode.equals("J"))
		{
			
		String op= operation[1];
		String ope[] = op.split("#");
		int destination = Integer.parseInt(ope[1]);
		res.vj= destination;
		programCounter = res.vj;
	    int index = progCounter.indexOf(Integer.valueOf(programCounter));
	    execute = rows[index];
	    iexecute(rows,index, execute, cycle,registers, dataSegment, programCounter, res, progCounter);
	
		
		
		//programCounter = destination;
		} // end of else if for jump

		else {
			String op1 = operation[1];
			String op2 = operation[2];
			String op3 = operation[3];

			int result = 0;

			if (opcode.equals("ADDI") || opcode.equals("ADDIU") || opcode.equals("SLTI"))// for
			// instruc
			// with r1 r2
			// #somevalue
			// type
			{
				String[] oper1 = op1.split(",");
				String[] oper2 = op2.split(",");
				String[] oper3 = op3.split("#");

				String opera1 = oper1[0];
				String opera2 = oper2[0];
				int operand3 = Integer.parseInt(oper3[1]);// has immediate value
															// to use

				String[] operan1 = opera1.split("R");
				String[] operan2 = opera2.split("R");

				int operand1 = Integer.parseInt(operan1[1]);// has register
															// index
				int operand2 = Integer.parseInt(operan2[1]);// has register
															// index
				res.vk= registers[operand2];

				if (opcode.equals("ADDI")) {
				//	registers[operand1] = registers[operand2] + operand3;
					res.vj= res.vk + operand3;
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
					

				}

				else if (opcode.equals("ADDIU")) {
					//registers[operand1]= registers[operand2] + operand3;
					res.vj= res.vk + operand3;
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
				
				}

				else if (opcode.equals("SLTI")) {
					if (res.vk < operand3)
					{
						//registers[operand1] = 1;
						res.vj = 1;
						registers[operand1]= res.vj;
						programCounter = programCounter +4;
				
					} else 
					{
						//registers[operand1] = 0;
						res.vj= 0;
						registers[operand1]= res.vj;
						programCounter = programCounter +4;
						
					}

				}

			} // end of if addi, addi...

			else if (opcode.equals("ADD") || opcode.equals("ADDU") || opcode.equals("AND") || opcode.equals("OR")
					|| opcode.equals("SLT") || opcode.equals("SLTU") || opcode.equals("NOR") || opcode.equals("SUB")
					|| opcode.equals("SUBU") || opcode.equals("XOR"))
			{
				String[] oper1 = op1.split(",");
				String[] oper2 = op2.split(",");
				String[] oper3 = op3.split("R");

				String opera1 = oper1[0];
				String opera2 = oper2[0];
				int operand3 = Integer.parseInt(oper3[1]);

				String[] operan1 = opera1.split("R");
				String[] operan2 = opera2.split("R");

				int operand1 = Integer.parseInt(operan1[1]);// has register
															// index
				int operand2 = Integer.parseInt(operan2[1]);// has register
															// index
				res.vk= registers[operand2];
				if (opcode.equals("ADD")) {
				//	registers[operand1] = registers[operand2] + registers[operand3];
					res.vj = res.vk + registers[operand3];
					registers[operand1]=res.vj;
					programCounter = programCounter +4;
				
				}

				if (opcode.equals("ADDU")) {
					//registers[operand1] = registers[operand2] + registers[operand3];
					res.vj = res.vk + registers[operand3];
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
					
				}

				if (opcode.equals("AND")) {
				//	registers[operand1] = registers[operand2] & registers[operand3];
					res.vj =res.vk & registers[operand3];
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
				
				}

				if (opcode.equals("OR")) {
				//	registers[operand1] = registers[operand2] | registers[operand3];
					res.vj= res.vk | registers[operand3];
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
				
				}

				if (opcode.equals("SLT")) {
					if (res.vk < registers[operand3])
						//registers[operand1] = 1;
					{
					res.vj= 1;
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
				
					}
					else
						//registers[operand1] = 0;
						{
						res.vj= 0;
						registers[operand1]= res.vj;
						programCounter = programCounter +4;
					
						}

				}

				if (opcode.equals("SLTU")) {
					if (res.vk < registers[operand3])
					//	registers[operand1] = 1;
						{
						res.vj = 1;
						registers[operand1]= res.vj;
						programCounter = programCounter +4;
						
						}
					else
					//	registers[operand1] = 0;
						{
						res.vj = 0;
						registers[operand1]= res.vj;
						programCounter = programCounter +4;
						}

				}

				if (opcode.equals("NOR")) {
				//	registers[operand1] = ~(registers[operand2] | registers[operand3]);
					res.vj = ~(res.vk | registers[operand3]);
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
				
				}

				if (opcode.equals("SUB")) {
				//	registers[operand1] = registers[operand2] - registers[operand3];
					res.vj = res.vk - registers[operand3];
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
				
				}

				if (opcode.equals("SUBU")) {
				//	registers[operand1] = registers[operand2] - registers[operand3];
					res.vj=res.vk - registers[operand3];
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
					
				}

				if (opcode.equals("XOR")) {
				//	registers[operand1] = registers[operand2] ^ registers[operand3];
					res.vj= res.vk ^ registers[operand3];
					registers[operand1]= res.vj;
					programCounter = programCounter +4;
				
				}

			} // end of else if for add, sub,xor....

			else if (opcode.equals("BEQ") || opcode.equals("BNE"))  
			{

				String opera1 = operation[1];
				String opera2 = operation[2];
				String off = operation[3];
				String[] oper3 = off.split("#");
				String offst =oper3[1];
				int offset = Integer.parseInt(offst);//has offset 
				
				String[] operan1 = opera1.split("R");
				String[] operan2 = opera2.split("R");
				String ope1 =operan1[1];
				String ope2 =operan2[1];
				String[] oper1 = opera1.split(",");
				String[] oper2 = opera2.split(",");
				int operand1 =Integer.parseInt(operan1[1].split(",")[0]);//has operand1
				int operand2 =Integer.parseInt(operan2[1].split(",")[0]);//has operand2
				
				if(opcode.equals("BEQ"))
				{
					if(registers[operand1]==registers[operand2])
					{
						//programCounter= programCounter + offset;
						res.vk = offset;
						res.ad = programCounter;
						res.ad = res.ad +res.vk;
						programCounter = res.ad;
						int index = progCounter.indexOf(Integer.valueOf(programCounter));
						execute = rows[index];
						iexecute(rows,index, execute, cycle,registers, dataSegment, programCounter, res, progCounter);
												
				     
					}
				}
				
				if(opcode.equals("BNE"))
				{
					if(registers[operand1]!=registers[operand2])
					{
						//programCounter= programCounter + offset;
						res.vk = offset;
						res.ad = programCounter;
						res.ad = res.ad + res.vk;
						programCounter = res.ad;
						int index = progCounter.indexOf(Integer.valueOf(programCounter));
						execute = rows[index];
						iexecute(rows,index, execute, cycle,registers, dataSegment, programCounter, res, progCounter);
														
					}
					
				}
				
			}
			
			
			else if (opcode.equals("SRL")|| opcode.equals("SLL") || opcode.equals("SRA"))
			{	
				String opera1 = operation[1];
				String opera2 = operation[2];
				String off = operation[3];
				int offset = Integer.parseInt(off);//has offset 
				
				String[] operan1 = opera1.split("R");
				String[] operan2 = opera2.split("R");
				String ope1 =operan1[1];
				String ope2 =operan2[1];
				String[] oper1 = opera1.split(",");
				String[] oper2 = opera2.split(",");
				int operand1 =Integer.parseInt(operan1[0]);//has operand1
				int operand2 =Integer.parseInt(operan2[0]);;//has operand2
				
				
				if(opcode.equals("SRL"))
				{
					//registers[operand1] = registers[operand2] >>> offset;
					res.vk = offset;
					res.vj = registers[operand2];
					res.qj = res.vj >>> res.vk;
					registers[operand1] = res.qj;
					programCounter = programCounter +4;
				
				}
				
				if(opcode.equals("SLL"))
				{
				//	registers[operand1] = registers[operand2] << offset;
					res.vk = offset;
					res.vj = registers[operand2];
					res.qj = res.vj << res.vk;
					registers[operand1] = res.qj;
					programCounter = programCounter +4;
			
				}
				
				if(opcode.equals("SRA"))
				{
				//   registers[operand1] = registers[operand2] >> offset;	
					res.vk = offset;
					res.vj = registers[operand2];
					res.qj = res.vj >> res.vk;
					registers[operand1] = res.qj;
					programCounter = programCounter +4;
					
				}
				
				
			} // end of else if for REG, REG, OFF TYPE offset inclusive
				// instruction

			else if (opcode.equals("BGEZ") || opcode.equals("BGTZ") || opcode.equals("BLEZ") || opcode.equals("BLTZ"))
			{

				String[] oper3 = operation[2].split("#");
				String offst =oper3[1];
				int offset = Integer.parseInt(offst); // has offset
						
				String opera1 = operation[1];
				String[] operan1 = opera1.split("R");
				String oper1 = operan1[1];
				String[] ope1= oper1.split(",");
				int operand1= Integer.parseInt(ope1[0]);//has register index value
				
				if(opcode.equals("BGEZ"))
				{
					if(registers[operand1] ==0 || registers[operand1] > 0 )
					{
					//	programCounter = programCounter + offset;
					res.vk = offset;
					res.ad = programCounter;
					res.ad = res.ad + res.vk;
					programCounter = res.ad;
					int index = progCounter.indexOf(Integer.valueOf(programCounter));
				    iexecute(rows,index, rows[index], cycle,registers, dataSegment, programCounter, res, progCounter);
				  
						
					}
				}
				
				if(opcode.equals("BGTZ"))
				{
					if( registers[operand1] > 0 )
					{
					//	programCounter = programCounter + offset;
						res.vk = offset;
						res.ad = programCounter;
						res.ad = res.ad + res.vk;
						programCounter = res.ad;
						int index = progCounter.indexOf(Integer.valueOf(programCounter));
					    iexecute(rows,index, rows[index], cycle,registers, dataSegment, programCounter, res, progCounter);
					  
					}
				}
				
				if(opcode.equals("BLEZ"))
				{
					if(registers[operand1] ==0 || registers[operand1] < 0 )
					{
					//	programCounter = programCounter + offset;
						res.vk = offset;
						res.ad = programCounter;
						res.ad = res.ad + res.vk;
						programCounter = res.ad;
						int index = progCounter.indexOf(Integer.valueOf(programCounter));
					    iexecute(rows,index, rows[index], cycle,registers, dataSegment, programCounter, res, progCounter);
					
					}
				}
				
				if(opcode.equals("BLTZ"))
				{
					if(registers[operand1] < 0 )
					{
					//	programCounter = programCounter + offset;
						res.vk = offset;
						res.ad = programCounter;
						res.ad = res.ad + res.vk;
						programCounter = res.ad;
						int index = progCounter.indexOf(Integer.valueOf(programCounter));
					    iexecute(rows,index, rows[index], cycle,registers, dataSegment, programCounter, res, progCounter);
					 
					}	
				}
				
				
			} // end of else if for REG, OFF TYPE offset inclusive instruction

		} // end of outer else for 3 operand type functions
		  return programCounter;
	}// end of execute function

	public void iwriteBack(String opcode) 
	{	
		
	}

	public void icommit(String commit, ArrayList rs, ArrayList rob, String opcode, int cycle)
	{
		
	if(opcode.equals("LW"))
	{
		
	}
	
	else if(opcode.equals("SW"))
	{
		
	}
	
	else if(opcode.equals("NOP"))
	{
		
	}
	
	else if(opcode.equals("BREAK"))
	{
		
	}
	
	else if (opcode.equals("J"))
	{
		
	}
	
	else if (opcode.equals("ADDI"))
	{
		cycle++;
	}
	
	else if (opcode.equals("ADDIU"))
	{
		cycle++;
	}
	
	else if (opcode.equals("SLTI"))
	{
		cycle++;
	}
	
	else if (opcode.equals("ADD"))
	{
		cycle++;
	}
	
	else if(opcode.equals("ADDU"))
	{
		cycle++;	
	}
	
	else if(opcode.equals("AND"))
	{
		cycle++;	
	}
	
	else if (opcode.equals("OR"))
	{
		cycle++;
	}
	
	else if(opcode.equals("SLT"))
	{
		cycle++;
	}
	
	else if (opcode.equals("SLTU"))
	{
		cycle++;
	}
	
	else if (opcode.equals("NOR"))
	{
		cycle++;
	}
	
	else if(opcode.equals("SUB"))
	{
		cycle++;
	}
	
	else if(opcode.equals("SUBU"))
	{
		cycle++;
	}
	
	else if (opcode.equals("XOR"))
	{
		cycle++;	
	}
	
	else if (opcode.equals("BEQ"))
	{
		cycle++;
	}
	
	else if (opcode.equals("BNE"))
	{
		cycle++;
	}
	
	else if (opcode.equals("SRL"))
	{
		cycle++;
	}
	
	else if(opcode.equals("SLL"))
	{
		cycle++;	
	}
	
	else if (opcode.equals("SRA"))
	{
		cycle++;
	}
	
	else if (opcode.equals("BGEZ"))
	{
		cycle++;
	}
	
	else if (opcode.equals("BGTZ"))
	{
		cycle++;
	}
	
	else if (opcode.equals("BLEZ"))
	{
		cycle++;
	}
	
	else if (opcode.equals("BLTZ"))
	{
		cycle++;
	}
	rob.remove(commit);
	rs.remove(commit);
	
	
	}//end of commit

	private static String ouputFile;
	private static PrintWriter writer = null;

}// end of class
