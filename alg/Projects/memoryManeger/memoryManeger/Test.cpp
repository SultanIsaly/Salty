#include "MemoryManeger.h"
#include <iostream>

using namespace std;
using namespace good_morning;



struct SMyInt
{
	int m_ind;

	SMyInt(int _ind)
		:m_ind(_ind)
	{}

	SMyInt()
	{}

	bool operator== (const SMyInt other) const
	{
		return (m_ind == other.m_ind);
	}
};

void testManagerWithFalse()
{
	const int n = 10000;
	const int blockSize = 1000;
	CMemoryManager<SMyInt> M(blockSize, false);
	SMyInt* array[2 * n];
	for (int i = 0; i < n; ++i)
	{
		array[i] = M.newObject();
		array[i]->m_ind = i;
	}

	for (int i = 0; i < n; i = i + 2)
	{
		M.deleteObject(array[i]);
	}

	for (int i = n; i < 2 * n; ++i)
	{
		array[i] = M.newObject();
		array[i]->m_ind = i;
	}
	for (int i = 1; i < 2 * n; i = i + 2)
	{
		M.deleteObject(array[i]);
	}

	try
	{
		M.clear();
	}
	catch (CException)
	{
		std::cout << "catched\n";
	}

	for (int i = (n % 2 == 0) ? n : (n - 1); i < 2 * n; i = i + 2)
	{
		M.deleteObject(array[i]);
	}
	M.clear();
}

void testManagerWithTrue()
{
	const int n = 10000;
	const int blockSize = 1000;
	CMemoryManager<SMyInt> M(blockSize, true);
	SMyInt* array[2 * n];
	for (int i = 0; i < n; ++i)
	{
		array[i] = M.newObject();
		array[i]->m_ind = i;
	}

	for (int i = 0; i < n; i = i + 2)
	{
		M.deleteObject(array[i]);
	}

	for (int i = n; i < 2 * n; ++i)
	{
		array[i] = M.newObject();
		array[i]->m_ind = i;
	}
	for (int i = 1; i < 2 * n; i = i + 2)
	{
		M.deleteObject(array[i]);
	}

	M.clear();
}


int main()
{
	testManagerWithFalse();
	testManagerWithTrue();
	system("pause");
	return 0;
}
