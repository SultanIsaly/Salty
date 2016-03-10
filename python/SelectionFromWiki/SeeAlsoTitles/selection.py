import requests

def sectionTitleList(titles):
    param = {
            'action':'parse',
            'page': titles,
            'format':'json',
            'prop':'sections',
            }
    ApiEndPoint = 'https://en.wikipedia.org/w/api.php'
    resp = requests.get(ApiEndPoint, params = param)
    sections = resp.json()['parse']['sections']
    titleSections = list()
    for section in sections:
        titleSections.append(section['anchor'])
    return titleSections   
    
def sectionContent(titleSection,pageTitle):
    ApiEndPoint = 'https://en.wikipedia.org/w/api.php'
    sections = sectionTitleList(pageTitle)
    number = sections.index(titleSection)
    param = {
            'action': 'query',
            'titles': pageTitle,
            'format':'json',
            'prop':'revisions',
            'rvprop':'content',
            'rvsection':number + 1
            }
    resp = requests.get(ApiEndPoint, params = param)
    section = resp.json()['query']['pages']
    section = section[list(section)[0]]['revisions']
    section = section[0]
    return section['*']