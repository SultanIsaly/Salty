#include <iostream>
#include <string>
#include <exception>
#include "singlelinkedlist.h"

using namespace magic_morning;

const static int N = 2;

struct MyStruct
{
	int number;
	int value;
	std::string str;

	MyStruct (int _number, int _value, std::string _str = "grow")
		: number(_number)
		, value(_value)
		, str(_str)
	{}

	MyStruct(int _number)
		: number(_number)
	{}

	MyStruct(const MyStruct& src)
		: number(src.number)
		, value(src.value)
		, str(src.str)
	{}

	bool operator!= (const MyStruct& src)
	{
		return number != src.number;
	}

	~MyStruct ()
	{}
};

void testSimplePushAndPop()
{
	CSingleLinkedList<MyStruct> list;
	for (size_t i = 0; i < N; ++i)
	{
		list.pushFront(MyStruct(i));
		list.pushBack(MyStruct(i));
	}
	if (list.getSize() != 2 * N)
	{
		std::cout << "WRONG\n";
		return;
	}
	for (size_t i = 1 ; i < 2 * N + 1; ++i) 
	{
		MyStruct m_str = list.popFront();
		if (i <= N && m_str.number != N - i)
		{
			std::cout << "WRONG\n";
			return;
		}
		else if (i > N && m_str.number != i - N - 1)
		{
			std::cout << "WRONG\n";
			return;
		}
	}
	std::cout << "OK\n";
}

void testSimpleIterateAndClear()
{
	CSingleLinkedList<MyStruct> list;
	for (size_t i = 0; i < N; ++i)
	{
		list.pushBack(MyStruct(i));
	}
	CSingleLinkedList<MyStruct>::CIterator it = list.begin();
	size_t i = 0;
	while (it != list.end())
	{
		MyStruct src = it.getData();
		if (src.number != i)
		{
			std::cout << "WRONG\n";
			return;
		}
		++i;
		++it;
	}
	list.clear();
	if (list.getSize() != 0)
	{
		std::cout << "WRONG\n";
		return;
	}
	std::cout << "OK\n";
}

void testErase()
{
	CSingleLinkedList<MyStruct> list;
	for (int i = 0; i < N; ++i)
	{
		list.pushFront(MyStruct(i));
	}
	CSingleLinkedList<MyStruct>::CIterator it = list.begin();
	list.erase(it);
	++it;
	if (it != list.begin())
	{
		std::cout << "WRONG\n";
		return;
	}
	while (it != list.end())
	{
		list.erase(it);
		++it;
	}
	list.erase(it);
	if (list.getSize() != 0)
	{
		std::cout << "WRONG\n";
		return;
	}
	std::cout << "OK\n";
}


int main()
{
	std::cout << "Test 1 - ";
	testSimplePushAndPop();

    std::cout << "Test 2 - ";
	testSimpleIterateAndClear();
	

	std::cout << "Test 3 - ";
	testErase();

	system("pause");
	return 0;
}
