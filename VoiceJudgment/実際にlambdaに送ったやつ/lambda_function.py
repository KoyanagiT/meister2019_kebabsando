import json
import Hideyoshi
import boto3
import logging
import urllib.parse

s3 = boto3.resource('s3')

def lambda_handler(event, context):
    bucket = event['Records'][0]['s3']['bucket']['name']
    key = urllib.parse.unquote_plus(event['Records'][0]['s3']['object']['key'], encoding='utf-8')
    #送られてくるデータ

    # set logging
    logger = logging.getLogger()
    logger.setLevel(logging.INFO)

    file_path = '/tmp/' + key

    bucket = s3.Bucket(bucket)
    bucket.download_file(key, file_path)
        
    #set "bucket name" and "object name"
    #BUCKET = "taikouwav"
    #TEST_FILE   = "sample.wav"
    
    #res = Hideyoshi.Hideyoshi(key)
    
    # get s3 object
    try:
        res = Hideyoshi.Hideyoshi(file_path)
        if res == "unknown data":
            message_code = "愉快"
            res = "安全"
        else:
            message_code = "不愉快"
    except Exception as e:
        return logger.error("Failed to get object: {}".format(e))
        
    return {
        "judge": message_code,
        "message": "this data is {}".format(res)
    }
