//import os
//import fileinput
//
//def list_files_walk(start_path='.'):
//    for root, dirs, files in os.walk(start_path):
//        for file in files:
//            newf = []
//#             print(os.path.join(root, file))
//            for line in fileinput.input(os.path.join(root, file)):
//#                 print(line,end='')
//                newf.append("//"+line)
//            file2 = open(os.path.join(root, file),"r+")
//            file2.writelines(newf)
//            file2.close()
//
//
//# Specify the directory path you want to start from
//directory_path = './'
//list_files_walk(directory_path)