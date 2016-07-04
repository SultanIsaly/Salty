#include <string>
#include <iostream>
#include "CDualLinkedList.h"

using namespace magic_morning;

const int n = 1000;

struct MyStruct
{
	int m_number;
	std::string m_value;

	MyStruct (int _number, std::string _value = "simple")
		: m_number(_number)
		, m_value(_value)
	{}

	MyStruct (const MyStruct& src)
		: m_number(src.m_number)
		, m_value(src.m_value)
	{}

	bool operator!= (const MyStruct& src)
	{
		return m_number != src.m_number;
	}

	~MyStruct ()
	{}
};

void testSimplePushAndIterator()
{
	CDoublyLinkedList<MyStruct> list;
	for (int i = 0; i < n; ++i)
	{
		list.pushFront(MyStruct(i));
	}
	for (int i = 0; i < n; ++i)
	{
		list.pushBack(MyStruct(i));
	}
	if (list.getSize() != 2 * n)
	{
		std::cout << "WRONG\n";
		return;
	}
	CDoublyLinkedList<MyStruct>::CIterator it = list.begin();
	for (int i = 0; i < 2 * n; ++i,++it)
	{
		if ( i < n && (*it).m_number != n - 1 - i)
		{
			std::cout << "WRONG\n";
			return;
		}
		if (i >= n && (*it).m_number != i - n)
		{
			std::cout << "WRONG\n";
			return;
		}
	}
	std::cout << "OK\n";
}

void testSimplePop()
{
	CDoublyLinkedList<MyStruct> list;
	for (int i = 0; i < 2*n; ++i)
	{
		list.pushBack(MyStruct(i));
	}
	for (int i = 0; i < n; ++i)
	{
		if (list.popFront().m_number != i)
		{
			std::cout << "WRONG\n";
			return;
		}
	}
	if (list.getSize() != n)
	{
		std::cout << "WRONG\n";
		return;
	}
	for (int i = 0; i < n; ++i)
	{
		if (list.popFront().m_number != i + n)
		{
			std::cout << "WRONG\n";
			return;
		}
	}
	if (list.getSize() != 0)
	{
		std::cout << "WRONG\n";
		return;
	}
	std::cout << "OK\n";
}

void clearTest()
{
	CDoublyLinkedList<MyStruct> list;
	for (int i = 0; i < n; ++i)
	{
		list.pushBack(MyStruct(i));
	}
	if (list.getSize() != n)
	{
		std::cout << "WRONG\n";
		return;
	}
	list.clear();
	if (list.getSize() != 0)
	{
		std::cout << "WRONG\n";
		return;
	}
	std::cout << "OK\n";
}

void testEraseAndPreBegin()
{
	CDoublyLinkedList<MyStruct> list;
	for (int i = 0; i < n; ++i)
	{
		list.pushFront(MyStruct(i));
	}
	CDoublyLinkedList<MyStruct>::CIterator it = list.begin();
	list.erase(it);
	++it;
	if (it != list.begin())
	{
		std::cout << "WRONG\n";
		return;
	}
	std::cout << "OK\n";
}

void testEraseNextAndPostEnd()
{
	CDoublyLinkedList<MyStruct> list;
	for (int i = 0; i < n; ++i)
	{
		list.pushFront(MyStruct(i));
	}
	CDoublyLinkedList<MyStruct>::CIterator it = list.end();
	--it;
	list.eraseAndNext(it);
	if (it != list.end())
	{
		std::cout <<"WRONG\n";
		return;
	}
	it = list.begin();
	list.eraseAndNext(it);
	if (it != list.begin())
	{
		std::cout << "WRONG\n";
		return;
	}
	if (list.getSize() != n - 2)
	{
		std::cout << "WRONG\n";
		return;
	}
	std::cout << "OK\n";
}

void testIterator()
{
	CDoublyLinkedList<MyStruct> list; 
	for (int i = 0; i < n; ++i)
	{
		list.pushFront(MyStruct(i));
	}
	CDoublyLinkedList<MyStruct>::CIterator it = list.begin();
	for (int i = 0; it != list.end(); ++it, ++i)
	{
		if (it.getData().m_number != n - i - 1)
		{
			std::cout << "WRONG\n";
			return;
		}
	}
	std::cout << "OK\n";
}

void testEraseAndInsert()
{
	CDoublyLinkedList<MyStruct> list;
	for (int i = 0; i < n; ++i)
	{
		list.pushFront(MyStruct(i));
	}
	CDoublyLinkedList<MyStruct>::CIterator it = list.begin();
	for (int i = 0; i < 100; ++i)
	{
		++it;
	}
	MyStruct new_str(-1);
	list.erase(it);
	list.insert(it, new_str);
	++it;
	if (it.getData().m_number != new_str.m_number)
	{
		std::cout << "WRONG\n";
		return;
	}

	it = list.begin();
	--it;
	new_str = MyStruct(-1);
	list.insert(it, new_str);
	if (list.begin().getData().m_number != -1)
	{
		std::cout << "WRONG\n";
		return;
	}

	it = list.end();
	--it;
	new_str = MyStruct(n);
	list.insert(it, new_str);
	++it;
	if (it.getData().m_number != n)
	{
		std::cout << "WRONG\n";
		return;
	}
	++it;
	if (it != list.end())
	{
		std::cout << "WRONG\n";
		return;
	}
	std::cout << "OK\n";
}

int main()
{
	std::cout << "Test 1 is ";
	testSimplePushAndIterator();
	std::cout << "Test 2 is ";
	testSimplePop();
	std::cout << "Test 3 is ";
	clearTest();
	std::cout << "Test 4 is ";
	testEraseAndPreBegin();
	std::cout << "Test 5 is ";
	testEraseNextAndPostEnd();
	std::cout << "Test 6 is ";
	testIterator();
	std::cout << "Test 7 is ";
	testEraseAndInsert();
	system("pause");
	return 0;
}