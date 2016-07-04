#include <iostream>
#include <exception>

namespace magic_morning
{
	class MyException : public std::exception
	{

		char* message;

	public:
		MyException()
			: message(0)
		{}


		MyException(char* str)
			: message(str)
		{
			throw message;
		}

		void
			getException(char* str)
		{
			throw message;
		}

	};
}
