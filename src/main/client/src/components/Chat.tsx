import React, { useState } from 'react';
import {Env} from '../Env';
import {Button, Input, List, Space} from 'antd';
import VirtualList from 'rc-virtual-list';

interface Message {
    type: 'user' | 'server';
    text: string;
}

const ContainerHeight = 300;

const ChatComponent: React.FC = () => {
    const [input, setInput] = useState<string>('');
    const [messages, setMessages] = useState<Message[]>([]);

    const handleSendMessage = async () => {
        if (!input.trim()) return;

        // Add user message to the chat
        const newMessages = [...messages, { type: 'user', text: input }] as Message[];
        setMessages(newMessages);
        setInput('');

        try {
            const response = await fetch(`${Env.API_BASE_URL}/chat`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({ message: input }),
            });
            if (!response.ok) {
                throw new Error('Failed to fetch response from server');
            }
            const data = await response.json();
            const serverMessage = data.message; // Assuming the server response has a 'reply' field

            // Add server response to the chat
            setMessages(prevMessages => [...prevMessages, { type: 'server', text: serverMessage }]);
        } catch (error) {
            console.error(error);
            setMessages(prevMessages => [...prevMessages, { type: 'server', text: 'Error: Unable to fetch response' }]);
        }
    };

    // const onScroll = (e: React.UIEvent<HTMLElement, UIEvent>) => {
    //     // Refer to: https://developer.mozilla.org/en-US/docs/Web/API/Element/scrollHeight#problems_and_solutions
    //     if (Math.abs(e.currentTarget.scrollHeight - e.currentTarget.scrollTop - ContainerHeight) <= 1) {
    //         // appendData();
    //     }
    // };

    return (
        <>
            <List>
                <VirtualList
                    data={messages}
                    height={ContainerHeight}
                    itemHeight={247}
                    itemKey="email"
                    // onScroll={onScroll}
                >
                    {(item: Message, index: number) => (
                        // <List.Item key={item.email}>
                        <List.Item key={index}>
                            <List.Item.Meta
                                // avatar={<Avatar src={item.picture.large} />}
                                // title={<a href="https://ant.design">{item.name.last}</a>}
                                // description={item.email}
                                title={<a href="https://ant.design">{item.type}</a>}
                                description={item.text}
                            />
                            <div>Content</div>
                        </List.Item>
                    )}
                </VirtualList>
            </List>

            <Space.Compact style={{ width: '100%' }}>
                <Input placeholder="Type your message..." onPressEnter={handleSendMessage} value={input} onChange={(e) => setInput(e.target.value)}/>
                <Button type="primary" onClick={handleSendMessage}>Send</Button>
            </Space.Compact>
        </>
    );
};

export default ChatComponent;
