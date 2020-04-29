import utils.*

def chapterName = 'chapter-salesforce'
  
listView(chapterName) {
    jobs {
      	regex("${chapterName}.+")
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

new GithubPipeline()
    .viewPrefix('chapter')
    .repository('salesforce')
    .includes('master quality dev')
    .jenkinsfileLocation('Jenkinsfile')
    .build(this)