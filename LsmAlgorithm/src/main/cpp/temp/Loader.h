// �U�C ifdef �϶��O�إߥ����H��U�q DLL �ץX���зǤ覡�C
// �o�� DLL �����Ҧ��ɮ׳��O�ϥΩR�O�C���ҩw�q ALOGRITHM_EXPORTS �Ÿ��sĶ���C
// �b�R�O�C�w�q���Ÿ��C����M�׳������w�q�o�ӲŸ�
// �o�ˤ@�ӡA��l�{���ɤ��]�t�o�ɮת������L�M��
// �|�N ALOGRITHM_API �禡�����q DLL �פJ���A�ӳo�� DLL �h�|�N�o�ǲŸ�����
// �ץX���C
#ifdef ALOGRITHM_EXPORTS
#define ALOGRITHM_API __declspec(dllexport)
#else
#define ALOGRITHM_API __declspec(dllimport)
#endif

// �o�����O�O�q Alogrithm.dll �ץX��
class ALOGRITHM_API Loader {
private:
	short twosComplement(short value, short bit);
public:
	Loader(void);
	~Loader(void);
	short* convertByteToShortArrayForMotion(char* array);
	short* convertByteToShortArrayForECG(char* array, int length);
};

