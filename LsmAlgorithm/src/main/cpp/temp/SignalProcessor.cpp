#include <math.h>
#include <stdlib.h> //free
#include <stdio.h> // using memset
#include "memory.h"
#include "algorithm"
#include "SignalProcessor.h"
#include <cmath>

using namespace std;

double *SignalProcessor::Smooth(double *data, int length, int order) {
	double* output;
	output = new double[length];

	if (order % 2 == 0) { 
		order = order - 1;
	}
	int rang = ((order - 1) / 2); 
	int bound = length - rang; 
	for (int i = 1; i < length - 1; i++)
	{
		if ((i + 1) <= rang && i>0)
		{
			int tempA = i % 2;
			int tempOrder;

			if (tempA == 0) tempOrder = i * 2 + 1;
			else tempOrder = i * 2 + 1;
			int temprRang = ((tempOrder - 1) / 2);
			double tempSum = 0.0;

			for (int j = i - temprRang; j <= i + temprRang; j++)
			{
				tempSum += data[j];
			}
			output[i] = tempSum / tempOrder;
		}
		else if (i > bound)
		{
			int a = length - i;
			int tempA = a % 2;
			int tempOrder;
			if (tempA == 0) tempOrder = a * 2 - 1;
			else tempOrder = a * 2 - 1;
			int temprRang = ((tempOrder - 1) / 2);
			double tempSum = 0.0;
			for (int j = i - temprRang; j <= i + temprRang; j++)
			{
				tempSum += data[j];
			}
			output[i] = tempSum / tempOrder;
		}
		else
		{
			double tempSum = 0.0;
			for (int j = i - rang; j <= i + rang; j++)
			{
				tempSum += data[j];
			}
			output[i] = tempSum / order;
		}
	}
	output[0] = data[0]; //Don't do nothing at first day
	output[length - 1] = data[length - 1]; //Don't do nothing at first day

	return output;
}

double *SignalProcessor::MiddleFilter(double *data, int length, int order) {
	int indexCorr = order - 1;
		int bufferlength = length + order;
	double window_1 = 0.0;

	double* output;
	output = new double[length];
	double* buffer = new double[bufferlength];
	memset(buffer, 0, sizeof(double)*bufferlength);

	for (int i = 0; i < bufferlength; i++) // copy data[0] to buffer[0:order] 
	{

		if (i < (order)) buffer[i] = data[0];
		else buffer[i] = data[i - indexCorr];
	}
	for (int i = 0; i < length; i++)
	{
		for (int j = i; j < i + order; j++)
			window_1 += buffer[j];
		double beasline = buffer[i + indexCorr] - ((window_1) / order);
		output[i] = beasline;
		window_1 = 0;
	}
	free(buffer);
	return output;
}

double SignalProcessor::Mean(double *data, int length) {
	double g_i64y = 0;
	for (int i = 0; i < length; i++) {
		g_i64y += data[i];
	}
	return g_i64y / length;
}

void SignalProcessor::Max(int *output, int *data, int length) {
	output[0] = data[0]; output[1] = 0;
	for (int i = 0; i < length - 1; i++) {
		if (data[(i + 1)] > output[0]) {
			output[0] = data[(i + 1)];
			output[1] = (i + 1);
		}
	}
}

void SignalProcessor::Min(int *output, int *data, int length) {
	output[0] = data[0]; output[1] = 0;
	for (int i = 0; i < length - 1; i++) {
		if (data[(i + 1) % length] < output[0]) {
			output[0] = data[(i + 1)];
			output[1] = (i + 1);
		}
	}
}

double *SignalProcessor::Diff(double *data, int length) {
	// input: data (ECGdata) 
	// length : ECGdata length
	// target : start index
	// output : 1st order differential
	double* output = new double[length];
	for (int i = 0; i<length; i++) output[i] = data[i];
	
	for (int i = 1; i < (length  + 1); i++) {
		output[i%length] = data[i%length] - data[(i - 1) % length];
	}
	output[0] = 0;
	return output;
}

double *SignalProcessor::CenterDiff(double *data, int length) {
    // : 5 point center differentiation
    // input: data (ECGdata) 
	// length : ECGdata length
	// target : start index
	// output : 5 point center differentiation
	double* output = new double[length];
	int bufferlength = length + 4;
	double* buffer = new double[bufferlength];
	buffer[0] = buffer[1] = buffer[bufferlength - 2] = buffer[bufferlength - 1] = 0;
	for (int i = 2; i < (bufferlength - 2); i++)
	{		buffer[i] = data[i - 2];	}

	for (int i = 2; i < (bufferlength-2); i++) {
		int data_index = i - 2;	
		double after = buffer[(i - 2) ] + 2 * buffer[(i - 1) ];
		output[data_index] = (1.0 / 6.0)*(2 * buffer[(i + 1) ] + buffer[(i + 2) ] - after);
	}
	return output;
}

double *SignalProcessor::Composite(double *data, int length) {
	// input: data (ECGdata) 
	// length : ECGdata length
	// target : start index
	// output : f[x] 
	double* CcenterDiff = new double[length];
	double* Cdiff = new double[length];
	double* output = new double[length];
	CcenterDiff = CenterDiff(data, length); //centerDiff
	Cdiff = Diff(CcenterDiff, length); //diff
	for (int i = 0; i<length; i++)//Composite
	{
		output[i] = abs(CcenterDiff[i]) + abs(Cdiff[i]);
	}
	free(CcenterDiff);
	free(Cdiff);
	return output;
}

double SignalProcessor::AdaptiveThreshold(double *composite, int length, double ADscale, double upper, double lower) {
	// input: composite signal
	// length :  composite's length
	// ADscale : robust threshold value 
	// upper :  the upper bound of adative threshold
	// lower :  the lower bound of adative threshold
	// output : AdaptiveThreshold
	double ADThreshold = 0;
	double ad_sum = 0;
	sort(composite, composite + length);
	reverse(composite, composite + length);
	int QRS_Interval = (int)floor(length*ADscale);
	int start_point = (int)floor(QRS_Interval*upper)-1;
	int end_point = (int)floor(QRS_Interval*lower-1);
	int count = 0;
	for (int i = start_point; i <= end_point; i++) {
		count++;
		ad_sum += composite[i];
	}

	ADThreshold = (ad_sum) / (end_point - start_point+1 );
	return ADThreshold;
}
