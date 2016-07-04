#include <iostream>
#include <vector>
#include <array>
#include <iomanip>
#include <fstream>
#include <algorithm>

#include "AVLTree.h"
using namespace good_morning;

static int n = 1000;
static int k = 10;

struct MyStruct {
	std::string key;
	std::string value;

	MyStruct() {}
	
	MyStruct(std::string _key, std::string _value)
		: key(_key)
		, value(_value)
	{}

	void setValue(std::string new_value)
	{
		value = new_value;
	}

	bool
	operator== (MyStruct& src)
	{
		bool result(key == src.key);
		return result;
	}

	bool
	operator!= (MyStruct& src)
	{
		bool result(!(*this == src));
		return result;
	}
};

std::string 
generateString (int num)
{
	const int maxLength = 30;
	const int alphabetSize = 52;
	const std::string alphabet = "abcdefghijklmnopqrstuwvxyzABCDEFGHIJKLMNOPQRSTUWVXYZ";
	int length = num % maxLength;
	std::string result;
	result.reserve(length);
	for (int i = 0; i < length; ++i)
	{
		result.push_back(alphabet[rand() % alphabetSize]);
	}
	return result;
}

int cmp (const MyStruct *a, const MyStruct *b) {
	int result = a->key.compare(b->key);
	if (result == 0) {
		return a->key.compare(b->key);
	}
	else {
		return result;
	}
}

bool compare(const MyStruct &a, const MyStruct &b) {
	return cmp(&a, &b) < 0;
}

void addAndFindTest() {
	std::vector<MyStruct> vct;
	for (int i = 0; i < n; ++i)
	{
		vct.push_back(MyStruct(generateString(i), generateString(i)));
	}

	CAVLTree<MyStruct, cmp> tree;
	for (int i = 0; i < vct.size(); ++i)
	{
		tree.add(&vct[i]);
	}

	for (int i = 0; i < vct.size(); ++i)
	{
		MyStruct* result = tree.find(vct[i]);
		if (*result != vct[i])
		{
			std::cout << "Error in add and find." << '\n';
			return;
		}
	}

	std::cout << "Test passed" << '\n';
	return;
}

void testDelete()
{
	std::vector<MyStruct> vct;
	for (int i = 0; i < n; ++i) 
	{
		vct.push_back(MyStruct(generateString(i), generateString(i)));
	}

	CAVLTree<MyStruct, cmp> tree;
	for (int i = 0; i < vct.size() / 2; ++i)
	{
		tree.add(&vct[i]);
	}

	for (int i = 0; i < vct.size(); ++i)
	{
		bool removed = tree.remove(vct[i]);
		bool TrueRemove(removed == false && i < vct.size() / 2);
		bool FalseRemove(removed == true && i >= vct.size() / 2);
		if (TrueRemove && FalseRemove)
		{
			std::cout << "Error in Remove" << '\n';
			return;
		}
	}
	//check removed elements
	for (int i = 0; i < vct.size() / 2; ++i)
	{
		if (tree.remove(vct[i]) == true)
		{
			std::cout << "Error in Remove, element not removed." << '\n';
			return;
		}
	}

	std::cout << "Test passed." << '\n';
	return;
}

void testUpdateAndClear()
{
	std::vector<MyStruct> vct;
	for (int i = 0; i < n; ++i) 
	{
		vct.push_back(MyStruct(generateString(i), generateString(i)));
	}

	CAVLTree<MyStruct, cmp> tree;
	for (int i = 0; i < vct.size(); ++i)
	{
		tree.add(&vct[i]);
	}
	MyStruct element(genetate);

	for (int i = 0; i < vct.size(); ++i)
	{

		bool update = tree.update(&vct[i]);
		if (update == false)
		{
			std::cout << "Error in Update.";
			return;
		}
	}

	tree.remove(vct[0]);
	if (tree.update(&vct[0]) == true)
	{
		std::cout << "Error in update not exist element.";
		return;
	}

	tree.clear();

	std::cout << "Test passed." << "\n";
	return;

}

int main ()
{
	addAndFindTest();
	testDelete();
	testUpdateAndClear();
	system("pause");
    return 0;
}