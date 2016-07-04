#ifndef MYEXCEPTION_2016_04_12
#define MYEXCEPTION_2016_04_12

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
#endif //  ifndef MYEXCEPTION_2016_04_11 

