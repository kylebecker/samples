/* Generated by Streams Studio: April 25, 2015 10:26:31 PM EDT */
package com.acme.test;


import java.io.IOException;

import org.apache.log4j.Logger;

import com.ibm.streams.operator.AbstractOperator;
import com.ibm.streams.operator.OperatorContext;
import com.ibm.streams.operator.OutputTuple;
import com.ibm.streams.operator.StreamingData.Punctuation;
import com.ibm.streams.operator.StreamingInput;
import com.ibm.streams.operator.StreamingOutput;
import com.ibm.streams.operator.Tuple;
import com.ibm.streams.operator.model.InputPortSet;
import com.ibm.streams.operator.model.InputPortSet.WindowMode;
import com.ibm.streams.operator.model.InputPortSet.WindowPunctuationInputMode;
import com.ibm.streams.operator.model.InputPorts;
import com.ibm.streams.operator.model.OutputPortSet;
import com.ibm.streams.operator.model.OutputPortSet.WindowPunctuationOutputMode;
import com.ibm.streams.operator.model.OutputPorts;
import com.ibm.streams.operator.model.PrimitiveOperator;
import com.ibm.streams.operator.state.Checkpoint;
import com.ibm.streams.operator.state.StateHandler;

/**
 * Class for an operator that receives a tuple and then optionally submits a tuple. 
 * This pattern supports one or more input streams and one or more output streams. 
 * <P>
 * The following event methods from the Operator interface can be called:
 * </p>
 * <ul>
 * <li><code>initialize()</code> to perform operator initialization</li>
 * <li>allPortsReady() notification indicates the operator's ports are ready to process and submit tuples</li> 
 * <li>process() handles a tuple arriving on an input port 
 * <li>processPuncuation() handles a punctuation mark arriving on an input port 
 * <li>shutdown() to shutdown the operator. A shutdown request may occur at any time, 
 * such as a request to stop a PE or cancel a job. 
 * Thus the shutdown() may occur while the operator is processing tuples, punctuation marks, 
 * or even during port ready notification.</li>
 * </ul>
 * <p>With the exception of operator initialization, all the other events may occur concurrently with each other, 
 * which lead to these methods being called concurrently by different threads.</p> 
 */
@PrimitiveOperator(name="JavaCalculator", namespace="com.acme.test",
description="Java Operator JavaCalculator")
@InputPorts({@InputPortSet(description="Port that ingests tuples", cardinality=1, optional=false, windowingMode=WindowMode.NonWindowed, windowPunctuationInputMode=WindowPunctuationInputMode.Oblivious), @InputPortSet(description="Optional input ports", optional=true, windowingMode=WindowMode.NonWindowed, windowPunctuationInputMode=WindowPunctuationInputMode.Oblivious)})
@OutputPorts({@OutputPortSet(description="Port that produces tuples", cardinality=1, optional=false, windowPunctuationOutputMode=WindowPunctuationOutputMode.Generating), @OutputPortSet(description="Optional output ports", optional=true, windowPunctuationOutputMode=WindowPunctuationOutputMode.Generating)})
// In order to support the consistent region feature, this operator must implement the
// StateHandler interface.
public class JavaCalculator extends AbstractOperator implements StateHandler {
	  // We will keep our intermediate calculator results in our member variables.
	  // When this C++ operator crashes and comes back, we will prove that the intermediate
	  // results will stay intact through our checkpoint and reset activities supported by the 
	  // consistent region feature.
	  int[] x = new int[500];
	  int[] y = new int[500];
	  String[] operation = new String[500];
	  int[] result = new int[500];
	  int tupleCnt;
	  
    /**
     * Initialize this operator. Called once before any tuples are processed.
     * @param context OperatorContext for this operator.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
	@Override
	public synchronized void initialize(OperatorContext context)
			throws Exception {
    	// Must call super.initialize(context) to correctly setup an operator.
		super.initialize(context);
        Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " initializing in PE: " + context.getPE().getPEId() + " in Job: " + context.getPE().getJobId() );
        
        // TODO:
        // If needed, insert code to establish connections or resources to communicate an external system or data store.
        // The configuration information for this may come from parameters supplied to the operator invocation, 
        // or external configuration files or a combination of the two.
        tupleCnt = 0;
	}

    /**
     * Notification that initialization is complete and all input and output ports 
     * are connected and ready to receive and submit tuples.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public synchronized void allPortsReady() throws Exception {
    	// This method is commonly used by source operators. 
    	// Operators that process incoming tuples generally do not need this notification. 
        OperatorContext context = getOperatorContext();
        Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " all ports are ready in PE: " + context.getPE().getPEId() + " in Job: " + context.getPE().getJobId() );
    }

    /**
     * Process an incoming tuple that arrived on the specified port.
     * <P>
     * Copy the incoming tuple to a new output tuple and submit to the output port. 
     * </P>
     * @param inputStream Port the tuple is arriving on.
     * @param tuple Object representing the incoming tuple.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public final void process(StreamingInput<Tuple> inputStream, Tuple tuple)
            throws Exception {
    	// 1) We will receive the calculation requests and perform the simple calculations.
    	// 
    	// 2) Results from those calculations will be kept inside the local 
    	// state (member) variables of this operator. We will not submit any result.  
    	// 
    	// 3) When we receive the 21st tuple, we will forcefully crash this operator.
    	// 
    	// 4) After recovery, everything should proceed normally.
    	//
    	// 5) When the final punctuation arrives, in the punctuation processing method below,
    	//    all the stored results in the local state variables will be sent at once.
    	//
    	// 6) User can check the final results to see if there are no gaps in the results due to
    	//    the operator failure and recovery process.
    	//
    	//
    	// If the newly arrived tuple is the 21st tuple, then we will forcefully crash this operator.
    	if ((tupleCnt == 20) && (getOperatorContext().getPE().getRelaunchCount() == 0)) {
    		// Force this operator to abort by throwing an exception.
    		throw(new Exception());
    	}
    	
    	// Create a new tuple for output port 0
    	/*
        StreamingOutput<OutputTuple> outStream = getOutput(0);
        OutputTuple outTuple = outStream.newTuple();

        // Copy across all matching attributes.
        outTuple.assign(tuple);

        // TODO: Insert code to perform transformation on output tuple as needed:
        // outTuple.setString("AttributeName", "AttributeValue");

        // Submit new tuple to output port 0
        outStream.submit(outTuple);
        */
  
    	int _result = 0;
    	int _x = tuple.getInt("x");
    	int _y = tuple.getInt("y");
    	String _operation = tuple.getString("operation");
    	// Perform the requested operation and compute the result.
    	if (_operation == "add") {
    		_result = _x + _y;
    	} else if (_operation == "subtract") {
    		_result = _x - _y;
    	} else if (_operation == "multiply") {
    		_result = _x * _y;
    	} else if (_operation == "divide") {
    		_result = _x / _y;
    	}
    	
    	// Save the results in our local state (member) variables. We are done.
    	// We will not submit any results at this time.
    	// Safeguard from array overruns.
    	if (tupleCnt > x.length) {
    		return;
    	}
    	
    	x[tupleCnt] = _x;
    	y[tupleCnt] = _y;
    	operation[tupleCnt] = _operation;
    	result[tupleCnt] = _result;
    	tupleCnt++;
    }
    
    /**
     * Process an incoming punctuation that arrived on the specified port.
     * @param stream Port the punctuation is arriving on.
     * @param mark The punctuation mark
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public void processPunctuation(StreamingInput<Tuple> stream,
    		Punctuation mark) throws Exception {
    	// When participating in a consistent region, we will never get FINAL_MARKER.
    	// Submit everything on receiving a regular punctuation marker.
    	if (mark == Punctuation.WINDOW_MARKER) {
			// This is the end of all tuple processing.
			// Let us now submit all the calculation results.
			for (int cnt = 0; cnt < tupleCnt; cnt++) {
	            StreamingOutput<OutputTuple> outStream = getOutput(0);
	            OutputTuple outTuple = outStream.newTuple();
				// This is the request sequence number attribute.
				outTuple.setInt("sequence", cnt+1);
				// This is x attribute
				outTuple.setInt("x", x[cnt]);
				// This is y attribute
				outTuple.setInt("y", y[cnt]);
				// This is operation attribute
				outTuple.setString("operation", operation[cnt]);
				// This is result attribute
				outTuple.setInt("result", result[cnt]);
				// Send it away.
				outStream.submit(outTuple); 
			}
    	}
    	
    	// For window markers, punctuate all output ports 
    	super.processPunctuation(stream, mark);
    }

    /**
     * Shutdown this operator.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    public synchronized void shutdown() throws Exception {
        OperatorContext context = getOperatorContext();
        Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " shutting down in PE: " + context.getPE().getPEId() + " in Job: " + context.getPE().getJobId() );
        
        // TODO: If needed, close connections or release resources related to any external system or data store.

        // Must call super.shutdown()
        super.shutdown();
    }

    // To support the consistent regions, this operator must implement the following methods.
	@Override
	public void close() throws IOException {
	}

	@Override
	// Persist state to shared file system
	public void checkpoint(Checkpoint checkpoint) throws Exception {
		checkpoint.getOutputStream().writeObject(x);
		/*
		System.out.println("Checkpointing x now: ");
		for (int cnt=0; cnt < x.length; cnt++) {
			System.out.print(x[cnt]);
		}
		*/
		
		checkpoint.getOutputStream().writeObject(y);
		checkpoint.getOutputStream().writeObject(operation);
		checkpoint.getOutputStream().writeObject(result);
		checkpoint.getOutputStream().writeObject(tupleCnt);
	}

	@Override
	// Submit any pending tuples before checkpoint happens.
	public void drain() throws Exception {
		// Do nothing.
	}

	@Override
	// Restore state from shared file system
	public void reset(Checkpoint checkpoint) throws Exception {
		try {
			// We must be coming back after a crash.
			// Read it back in the same order as it was checkpointed.
			x = (int[])checkpoint.getInputStream().readObject();
			/*
			System.out.println("Resetting x:");

			for (int cnt=0; cnt < x.length; cnt++) {
				System.out.print(x[cnt] + ", ");
			}			
			
			System.out.println("");
			*/
			
			y = (int[])checkpoint.getInputStream().readObject();
			operation = (String[])checkpoint.getInputStream().readObject();
			result = (int[])checkpoint.getInputStream().readObject();
			tupleCnt = (int)checkpoint.getInputStream().readObject();
			// System.out.println("Resetting tupeCnt = " + tupleCnt);
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
	}

	@Override
	// Sets operator state to its initial state
	// This is needed only when there is a crash anywhere in the application before the
	// very first checkpoint is done.	
	public void resetToInitialState() throws Exception {
		tupleCnt = 0;
	}

	@Override
	public void retireCheckpoint(long id) throws Exception {
		
	}
}