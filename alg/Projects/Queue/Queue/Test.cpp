#include <iostream>
#include <string>
#include "MyQueue.h"

using namespace magic_morning;

struct MyStruct
{
	int m_number;
	std::string m_value;

	MyStruct (int _number, std::string _value = "simple")
		: m_number (_number)
		, m_value (_value)
	{}

	MyStruct (const MyStruct& src)
		: m_number(src.m_number)
		, m_value (src.m_value)
	{}

	bool operator!= (const MyStruct& src)
	{
		return m_number != src.m_number;
	}

	~MyStruct ()
	{}
};

void sizeAndInQueueTest ()
{
	CQueue<MyStruct> queue;
	for (int i = 0; i < 1000; ++i)
	{
		queue.InQueue(MyStruct(i));
	}
	if (queue.getSize() != 1000)
	{
		std::cout << "SIZE_WRONG\n";
		return;
	}
	for (int i = 0; i < 1000; ++i)
	{
		if (queue.DeQueue() != i)
		{
			std::cout << "INQUEUE_WRONG\n";
			return;
		}
	}
	std::cout << "OK\n";
}

void clearAndIsQueueEmptyTest ()
{
	CQueue<MyStruct> queue;
	if (!queue.isQueueEmpty())
	{
		std::cout << "ISQUEUEEMPTY_WRONG\n";
		return;
	}
	for (int i = 0; i < 1000; ++i)
	{
		queue.InQueue(MyStruct(i));
	}
	queue.clear();
	if (queue.getSize() != 0)
	{
		std::cout << "CLEAR_WRONG\n";
		return;
	}
	std::cout << "OK\n";
}

int main()
{
	sizeAndInQueueTest();
	clearAndIsQueueEmptyTest();
	system("pause");
	return 0;
}