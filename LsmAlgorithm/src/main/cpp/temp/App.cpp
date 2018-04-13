
/*
 * authors: Moriarty, Zhen, Gaduo, Rouyi
 * There are some functions about Heart Rate Computing, Heat Computing, Pedometer.
 */


#include <iostream>
#include <fstream>
#include "Loader.h"
#include "HeartRate.h"
#include "Pedometer.h"
#include "Heat.h"

using namespace std;

void heart_rate();
void count_steps();
void count_heat();

int main(void) {
	heart_rate(); // �p��߲v�d��
	// count_heat(); // �p����q�d�ҡA�иѶ}����� 
	// count_steps(); // �p��B�ƽd�ҡA�иѶ}����� 
	system("Pause");
	return 0;
}

void heart_rate()
{
	/* ==================== �߲v�t��k ==================== */
    HeartRate* heart_rate = new HeartRate(250, 500); // �����W�v �P �B�̪����
	/* ==================== �߲v�t��k ==================== */
	

	string filename = "../dataset/ECG/System-ECG-20180323-112904.dat";
	Loader loader;
	int buffer_size = 2;
	fstream file;

	file.open(filename, ios::in | ios::binary);

	if(file.is_open()) {

		file.seekg(0, ios::end);
		streamoff file_size = file.tellg();

		file.seekg(0, ios::beg);
		for(int i = 0; i < (file_size/buffer_size); i++) {
			char* buffer = new char[buffer_size];
			file.read(buffer, buffer_size);

			// �C��� byte �X�֦� �@�� short Value
			short* data = loader.convertByteToShortArrayForECG(buffer, 1);
			
			/* ==================== �߲v�t��k ==================== */
			int hr = heart_rate->countHeartRate(data[0]);
			if (hr != -1)
			{
				cout << ", Heart Rate: " << hr << "\n";
			}
			/* ==================== �߲v�t��k ==================== */

			file.seekg(0, ios::cur);
		}
	}
}

void count_heat() 
{
	/* ==================== �߲v�t��k ==================== */
    HeartRate* heart_rate = new HeartRate(250, 500);
	/* ==================== �߲v�t��k ==================== */
	
	/* ====================�p����q�t��k ==================== */
	int w = 50; //�w�]��
	int age = 25;//�w�]��
	bool sex = false;//�w�]��
	Heat* heat = new Heat(w, age, sex);
	/* ====================�p����q�t��k ==================== */
	
	string filename = "../dataset/ECG/System-ECG-20180323-112904.dat";
	Loader loader;
	int buffer_size = 2;
	fstream file;

	file.open(filename, ios::in | ios::binary);

	if(file.is_open()) {

		file.seekg(0, ios::end);
		streamoff file_size = file.tellg();

		file.seekg(0, ios::beg);
		for(int i = 0; i < (file_size/buffer_size); i++) {
			char* buffer = new char[buffer_size];
			file.read(buffer, buffer_size);

			// �C��� byte �X�֦� �@�� short Value
			short* data = loader.convertByteToShortArrayForECG(buffer, 1);
			
			/* ==================== �߲v�t��k ==================== */
			int hr = heart_rate->countHeartRate(data[0]);
			/* ==================== �߲v�t��k ==================== */
			
			/* ==================== �p����q�t��k ==================== */
			
			if (hr != -1)
			{
				double t = 0.00056;
				double calories = heat->countHeatCalories(hr, t);
				//calories = calories +heat->countHeatCalories(hr, t); // ���q�����p���֥[
				cout << "Calories: "<<calories << "\n";
			}
			/* ==================== �p����q�t��k==================== */

			file.seekg(0, ios::cur);
		}
	}
}

void count_steps()
{
	/* ==================== �p�B���t��k ==================== */
	Pedometer* pedometer = new Pedometer(50, 25, 2, 500);
	/* ==================== �p�B���t��k ==================== */

	const string filename ="../dataset/Motion/gaduo@singularwings.com-Accelerator-20171127-204829.txt";

	/**
	 * A data composed by 19 bytes;
	 * | Gyro-xL | Gyro-xH | Gyro-yL | Gyro-yH | Gyro-zL | Gyro-zH |
	 * | Acc-xL  | Acc-xH  | Acc-yL  | Acc-yH  | Acc-zL  | Acc-zH  |
	 * | Mag-xL  | Mag-xH  | Mag-yL  | Mag-yH  | Mag-zL  | Mag-zH  |
	 * | idx     |
	 */
	Loader loader;
	const int buffer_size = 19;
	fstream file;

	file.open(filename, ios::in | ios::binary);

	if(file.is_open()) {

		file.seekg(0, ios::end);
		streamoff file_size = file.tellg();

		file.seekg(0, ios::beg);
		for(int i = 0; i < (file_size/buffer_size); i++) {
			char* buffer = new char[buffer_size];
			file.read(buffer, buffer_size);

			short* data = loader.convertByteToShortArrayForMotion(buffer);
			
			/* ==================== �p�B���t��k ==================== */
			const int steps = pedometer->countStep(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
			if (steps != -1)
			{
				cout << steps << "\n";
			}
			/* ==================== �p�B���t��k ==================== */

			file.seekg(0, ios::cur);
		}
	}
}