import making
import glob

if __name__ == "__main__":

    files = list(glob.glob("C:\\Users\\koyanagi\\Desktop\\VoiceMaker\\learning_sample\\*"))

    num = range(0,len(files))
    
    for file in files:
        print(file, num[files.index(file)])
        making.making(file, num[files.index(file)])
