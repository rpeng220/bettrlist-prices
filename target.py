import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.common.exceptions import NoSuchElementException
from bs4 import BeautifulSoup
from datetime import datetime
import time
import numpy as np
import os.path
from os import path
from selenium.webdriver.chrome.options import Options
import json

options = Options()
options.headless = True

driver = webdriver.Chrome(executable_path='C:/Users/qwert/chromedriver/chromedriver.exe')

URL = 'https://www.target.com/'


def existsid(id):
    try:
        driver.find_element_by_id(id)
    except NoSuchElementException:
        return False
    return True


def existsclass(classname):
    try:
        driver.find_element_by_class_name(classname)
    except NoSuchElementException:
        return False
    return True


pricebook = {}
itemid = 200001
df = pd.read_csv('grocerylist.csv')
grocerylist = df['Column1'].to_numpy()


if __name__ == '__main__':
    driver.get(URL)
    time.sleep(2)
    for item in grocerylist:
        if existsid('search'):
            driver.find_element_by_id('search').send_keys(Keys.CONTROL, "a")
            time.sleep(1)
            driver.find_element_by_id('search').send_keys(Keys.DELETE)
            time.sleep(1)
            driver.find_element_by_id('search').send_keys(item)
            driver.find_element_by_id('search').submit()
        time.sleep(5)
        productnames = driver.find_elements_by_xpath('//*[@data-test="product-title"]')
        prices = driver.find_elements_by_xpath('//*[@data-test="current-price"]')
        links = driver.find_elements_by_xpath('//*[@data-test="product-title"]')
        for i in np.arange(len(productnames)):
            currentitem = {'id': itemid, 'store': 'Target', 'product_name': productnames[i].text,
                           'price': prices[i].text, 'unit_price': '', 'product_link': links[i].get_attribute("href"),
                           'search_item': item}
            pricebook[itemid] = currentitem
            itemid += 1
            time.sleep(1)
        # currentitem['id'] = itemid
        # currentitem['store'] = 'Target'
        # # currentitem['store'] = driver.find_element_by_xpath("//*[@data-test='@web/StoreMessage/StoreName']/div").text
        # currentitem['product_name'] = driver.find_element_by_xpath('//*[@data-test="product-title"]').text
        # currentitem['price'] = driver.find_element_by_xpath('//*[@data-test="current-price"]').text
        # currentitem['unit_price'] = ''
        # link = driver.find_element_by_xpath('//*[@data-test="product-title"]').get_attribute("href")
        # currentitem['product_link'] = link
        # pricebook[item] = currentitem
        # itemid = itemid + 1
        time.sleep(1)
    json_object = json.dumps(pricebook, indent = 4)
    with open('target_prices.json', 'w') as outfile:
        outfile.write(json_object)

