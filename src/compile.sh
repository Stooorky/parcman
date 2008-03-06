#!/bin/bash


# Elenco dei pacchetti
pkglist=("pkgserver client tests database remoteexceptions databaseserver parcmanserver setup indexingserver all")

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

all=("clientbootstrap plog remoteclient tests database remoteexceptions databaseserver parcmanserver setup indexingserver")



#################
# PRIVATE
#################
# Funzione di compilazione package
compile()
{
	for var in $@
	do
		cd $var
		bash compile.sh
		cd ..
	done
}

# Funzione di clean package
clean()
{
	for var in $@
	do
		cd $var
		bash compile.sh clean
		cd ..
	done
}


if [ "$1" = "clean" ]; then
	# Clean
	for pkg in $pkglist
	do
		if [ "$2" = "$pkg" ]; then
			echo -e "\033[31;1mClean $pkg...\033[0m"
			clean ${!pkg}
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
			compile ${!pkg}
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

