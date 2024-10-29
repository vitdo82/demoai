import {useEffect, useState} from 'react'
import './App.css'
import {Env} from './Env'
import SingleFileUploader from './components/SingleFileUploader'
import ChatComponent from './components/Chat'

function App() {
  const [books, setBooks] = useState<string[]>([])

    useEffect(() => {
        fetch(`${Env.API_BASE_URL}/documents`)
            .then(response => response.json())
            .then(data => setBooks(data as string[]))
            .catch(error => console.error("Error fetching documents:", error));
    }, []);

  return (
    <>
        <div>
            {books.map((book, index) => {
                return <p key={index}>{book}</p>;
            })}
        </div>
        <div>
            <div>
                <h2>
                    Document Upload
                </h2>
                <div>
                    <SingleFileUploader/>
                </div>
            </div>
        </div>
        <div>
            <ChatComponent />
        </div>

    </>
  )
}

export default App
