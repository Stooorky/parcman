#!/bin/bash


# Elenco dei pacchetti
pkglist=("pkgserver client tests database remoteexceptions databaseserver parcmanserver parcmanclient loginserver privilege setup indexingserver pshell all parcmanagent")

# Pacchetti
pkgserver=("remoteclient plog")
client=("clientbootstrap plog")
tests=("tests")
database=("database plog")
remoteexceptions=("remoteexceptions")
databaseserver=("databaseserver remoteexceptions plog")
parcmanserver=("parcmanserver remoteexceptions plog")
setup=("setup")
indexingserver=("indexingserverm remoteexceptions plog")
loginserver=("loginserver remoteexceptions plog")
parcmanclient=("parcmanclient plog remoteexceptions")
privilege=("privilege")
pshell=("pshell")
parcmanagent=("parcmanagent")

all=("parcmanclient clientbootstrap plog remoteclient tests database remoteexceptions databaseserver loginserver parcmanserver pshell privilege setup indexingserver parcmanagent")

JAVAC_XLINT_FLAG="-Xlint"

#################
# PRIVATE
#################
# Funzione di compilazione package
main_compile()
{
	for var in $@
	do
		cd $var
		bash compile.sh
		cd ..
	done
}

# Funzione di clean package
main_clean()
{
	for var in $@
	do
		cd $var
		bash compile.sh clean
		cd ..
	done
}

# Funzione di compiazione con Xlint per i warning
main_xlint()
{
	for var in $@
	do
		cd $var 
		bash compile.sh xlint "$JAVAC_XLINT_FLAG"
		cd ..
	done
}

if [ "$1" == "clean" ]; then
	# Clean
	for pkg in $pkglist
	do
		if [ "$2" = "$pkg" ]; then
			echo -e "\033[31;1mClean $pkg...\033[0m"
			main_clean ${!pkg}
			echo -e "\033[31;1mDone.\033[0m"
			exit
		fi
	done
elif [ "$1" == "xlint" ]; then
	# Xlint 
	for pkg in $pkglist
	do
		if [ "$2" = "$pkg" ]; then
			echo -e "\033[31;1mCompile $pkg with -Xlint flag...\033[0m"
			main_xlint ${!pkg}
			echo -e "\033[31;1mDone.\033[0m"
			exit
		fi
	done
else
	# Compile
	for pkg in $pkglist
	do
		if [ "$1" = "$pkg" ]; then
			echo -e "\033[31;1mCompile $pkg...\033[0m"
			main_compile ${!pkg}
			echo -e "\033[31;1mDone.\033[0m"
			exit
		fi
	done
fi

#####################
# USE
#####################
echo "      For Clean:"
echo "       $0 clean <${pkglist}>"
echo "      For Compile:"
echo "       $0 <${pkglist}>"
echo "      For Compile with -Xlint flag:"
echo "       $0 <${pkglist}>"
