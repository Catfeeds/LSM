#pragma once
class Heat
{
private:
	int W;
	int A;
	bool is_girl;

public:
	Heat(int weight, int age, bool is_girl);
	~Heat(void);

	double countHeatCalories(int HR, double T); // �C 6 ��p��@�� T = 0.002
};

